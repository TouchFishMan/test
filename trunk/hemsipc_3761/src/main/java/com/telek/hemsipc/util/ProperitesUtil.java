package com.telek.hemsipc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProperitesUtil {
    private static Logger LOG = LoggerFactory.getLogger(ProperitesUtil.class);
    /**
     * 缓存文件夹路径
     */
    public static String ProFileSrc = System.getProperty("user.dir") + File.separator + "src" + File.separator
            + "main" + File.separator + "resources" + File.separator;

    /**
     * 获取工程根节点
     * 
     * @return
     * @author Xugl
     * @2017年9月15日 上午10:20:01
     */
    public static String getRoot() {
        String path = ProperitesUtil.class.getClassLoader().getResource("").getPath();
        return path.replace("%20", " ");
    }

    /**
     * Description:获取resource下Properties对象
     * 
     * @author lxf
     * @created 2018年1月19日 上午8:59:53
     * @param filePath
     * @return
     */
    public static Properties getProperties(String filePath) {
        Properties config = new Properties();
        try {
            InputStream is = new FileInputStream(filePath);
            config.load(is);
            is.close();
            return config;
        } catch (IOException e) {
            LOG.error("读取配置文件出错。");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: 获取resource下的文件全路径
     * 
     * @author lxf
     * @created 2018年1月19日 上午8:58:56
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName) {
        String pathString = getRoot() + fileName;
        File file = new File(pathString);
        if (!file.exists()) {
            pathString = ProFileSrc + fileName;
        }
        return pathString;
    }
}
