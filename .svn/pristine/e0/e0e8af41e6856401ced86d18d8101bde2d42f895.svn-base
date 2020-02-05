package com.telek.hemsipc.util;

import com.jcraft.jsch.*;

import java.io.File;
import java.util.Properties;

/**
 * @Auther: wll
 * @Date: 2018/9/11 13:28
 * @Description: SFTP工具
 */
public class SftpUtil {
    public static void downloadFile(String host, int port, String username, String password, String sftpPath, String localPath, String fileName) throws JSchException, SftpException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setTimeout(1000);
        Properties config = new Properties();
        //设置不用检查HostKey，设成yes，一旦计算机的密匙发生变化，就拒绝连接。
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        String localFilePath = localPath + File.separator;
        try {
            //使用ChannelSftp的get(文件名，本地路径{包含文件名})方法下载文件
            chSftp.get(fileName, localFilePath);
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
    }
}
