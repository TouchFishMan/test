package com.telek.hemsipc.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.service.IDBService;
import com.telek.hemsipc.service.impl.DBServiceImpl;
import com.telek.hemsipc.util.HttpUtil;
import com.telek.hemsipc.util.MD5Util;
import com.telek.hemsipc.util.SysCmdUtil;
import com.telek.hemsipc.util.ZipUtil;

/**
 * 升级线程
 *
 * @author zhanxf 2015-11-27 TODO 需要云端进行新的指令发送后开启新的固件升级
 * @Descrption
 */
public class FirmwareUpdateServer implements Runnable {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private String md5;
    private String downloadKey;

    public FirmwareUpdateServer(String md5, String downloadKey) {
        this.md5 = md5;
        this.downloadKey = downloadKey;
    }

    private IDBService dbService = HemsipcSpringContext.getBean(DBServiceImpl.class);
   // private ICloudService cloudService = HemsipcSpringContext.getBean(CloudServiceImpl.class);


    private void process() throws IOException, SQLException {
        // 1.从HTTP上下载zip包
        HttpUtil.downLoadFromUrl("http://127.0.0.1:8081/file/download", "update", "update.zip", downloadKey);
        // 2.通过md5校验下载包是否完整
        File file = new File("update/update.zip");
        String md5Ck = MD5Util.getFileMD5String(file);
        System.out.println(md5Ck);
        if (!md5.equals(md5Ck)) {
            log.error("【固件升级失败】升级包MD5校验失败");
            return;
        }
        // 3.解压zip包得到jar文件和数据库执行sql
        ZipUtil.unZip("update/update.zip", "update");
        // 4.通过shell执行备份操作
        SysCmdUtil.exeCmd("shell/backup.sh " + System.getProperty("user.dir") + " " + dbService.getDbType());
        // 5.从sql文件中获取sql，执行数据库sql
        try {
            List<String> sqls = getSqlsFromFile("update/update.sql");
            if (sqls != null && sqls.size() > 0) {
                dbService.executeSqls(sqls);
            }
        } catch (Exception e) {
            log.error("【固件升级失败】", e);
            //数据库执行失败，则直接回滚数据库
            SysCmdUtil.exeCmd("sh shell/recover.sh " + System.getProperty("user.dir") + " " + dbService.getDbType());
            return;
        }
        //通过shell脚本升级程序
        SysCmdUtil.exeCmd("sh shell/update.sh " + System.getProperty("user.dir") + " " + dbService.getDbType());
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log.error("【固件升级失败】", e);
        }
    }

    private List<String> getSqlsFromFile(String sqlFile) {
        List<String> sqls = new ArrayList<>();
        File file = new File(sqlFile);
        //如果sql文件不存在，则不执行sql
        if (!file.exists()) {
            return null;
        }
        file.deleteOnExit();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            String temp = null;
            StringBuffer sql = new StringBuffer();
            while ((temp = bufferedReader.readLine()) != null) {
                if (StringUtils.isEmpty(temp.trim())) {
                    continue;
                }
                sql.append(temp);
                if (temp.contains(";")) {
                    sqls.add(sql.toString());
                    sql = new StringBuffer();
                }
            }
        } catch (IOException e) {
            log.error("sql读取错误", e);
        }
        return sqls;
    }
}
