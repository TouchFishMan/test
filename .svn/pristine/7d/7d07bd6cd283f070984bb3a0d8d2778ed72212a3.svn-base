package com.telek.hemsipc.protocal3761.refresh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbsHotRefresh {

    public static final String ROOT_PATH = System.getProperty("user.dir") + "/conf/hot";

    protected static String getTerminalAddress(File file) throws IOException {
        if (file != null && file.exists()) {
            Properties properties = new Properties();
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                properties.load(inputStream);
                String terminalAddress = properties.get("terminalAddress").toString();
                return terminalAddress;
            }
        }
        return "";
    }

}
