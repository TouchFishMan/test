package com.telek.hemsipc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SysCmdUtil {
    private static Logger log = LoggerFactory.getLogger(SysCmdUtil.class);

    /**
     * @Description: 执行linux指令
     * @auther: wll
     * @date: 16:50 2018/9/25
     * @param: [cmd]
     * @return: int
     */
    public static int exeCmd(String cmd) {
        log.info("执行命令：{}", cmd);
        String[] comands = new String[]{"/bin/sh", "-c", cmd};
        try {
            Process process = Runtime.getRuntime().exec(comands);
            return process.waitFor();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * 执行系统命令
     *
     * @param cmd 返回执行的进程信息
     */
    public static String exeCmdGetReturn(String cmd) {
        log.info("执行命令：{}", cmd);
        String[] comands = new String[]{"/bin/sh", "-c", cmd};
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(comands);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            log.info("指令返回:" + sb);
            return sb.toString();
        } catch (Exception e) {
            log.error(e.toString(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取网络重启指令
     *
     * @return
     * @Modifier:kds
     * @Date：2018年6月7日上午11:19:19
     * @Describe:getNetworkRestartCommand
     */
    public static void netRestart() {
        String comand = "service network restart";

        //获取当前centos的版本号，centos6和7的网络重启指令不一样
        String centosVersion = exeCmdGetReturn("rpm -q centos-release");
        if (centosVersion.indexOf("centos-release-7") >= 0) {
            comand = "systemctl restart network";
        }
        exeCmd(comand);
    }
}
