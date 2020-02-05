package com.telek.hemsipc.service.impl;

import com.mysql.jdbc.JDBC4Connection;
import com.telek.hemsipc.service.IDBService;
import com.telek.hemsipc.util.SysCmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Auther: wll
 * @Date: 2018/9/25 11:16
 * @Description:
 */
@Service
public class DBServiceImpl implements IDBService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataSource dataSource;

    @Value("${spring.profiles.active}")
    private String dbType;

    public String getDbType() {
        return dbType;
    }

    @Override
    public void executeSqls(List<String> sqls) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            //循环从sql文件中读取sql语句，然后逐条执行
            for (String sql : sqls) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            connection.close();
        }
    }

    @Override
    public void dbbackup() throws SQLException {
        log.info("{}数据库备份开始", dbType);
        if (dbType.toLowerCase().equals("h2")) {
            //H2数据库备份
            exportH2Database();
        } else if (dbType.toLowerCase().equals("mysql")) {
            //mysql数据库备份
            exportMysqlDatabase();
        }
    }

    public void exportMysqlDatabase() throws SQLException {
        File saveFile = new File("backup");
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        JDBC4Connection connection = (JDBC4Connection) DataSourceUtils.getConnection(dataSource).getMetaData().getConnection();
        String host = connection.getHost();
        String username = connection.getUser();
        String password = connection.getPasswordCharacterEncoding();
        String dbname = connection.getDefaultAuthenticationPlugin();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mysqldump").append(" -h").append(host);
        stringBuilder.append(" --user=").append(username).append(" --password=").append(password).append(" --lock-all-tables=true");
        stringBuilder.append(" --result-file=").append("backup/db-backup.recently").append(" --default-character-set=utf8 ").append(dbname);
        //执行mysql备份指令
        if (SysCmdUtil.exeCmd(stringBuilder.toString()) == 0) {// 0 表示线程正常终止。
            log.info("mysql数据库备份成功");
        }
    }

    public void exportH2Database() {
        String cpCmd = "cp -r h2/ backup/h2/";
        String rmCmd = "rm -rf h2/";
        if (SysCmdUtil.exeCmd(cpCmd) == 0 && SysCmdUtil.exeCmd(rmCmd) == 0) {// 0 表示线程正常终止。
            log.info("H2数据库备份成功");
        }
    }
}
