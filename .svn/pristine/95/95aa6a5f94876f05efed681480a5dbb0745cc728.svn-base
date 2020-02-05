package com.telek.hemsipc.protocal3761.refresh;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.datamodel.Afn4F7Data;
import com.telek.hemsipc.protocal3761.service.request.SetAfn4F7TerminalIp;

public class Afn4F7HotRefresh extends AbsHotRefresh {

    private static Logger log = LoggerFactory.getLogger(Afn4F7HotRefresh.class);

    public static final String FILE_NAME = "afn4f7.properties";
    public static final String FILE_PATH = ROOT_PATH + "/" + FILE_NAME;
    public static final File FILE = new File(FILE_PATH);

    public static void refresh() {
        try {
            String terminalAddress = getTerminalAddress(FILE);
            String address2 = null;
            for (Map.Entry<String, String> entry : Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.entrySet()) {
                String eTerminalAddress = entry.getValue();
                if (eTerminalAddress.equalsIgnoreCase(terminalAddress)) {
                    address2 = entry.getKey();
                    break;
                }
            }
            if (address2 != null) {
                Afn4F7Data datas = getDatas();
                SetAfn4F7TerminalIp setAfn4F10Params = new SetAfn4F7TerminalIp(true, datas);
                setAfn4F10Params.dealSendToTerminal(address2, null);
            } else {
                log.error("未登录或离线：" + terminalAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Afn4F7Data getDatas() {
        Afn4F7Data afn4F7Data = new Afn4F7Data();
        if (FILE != null && FILE.exists()) {
            try (InputStream inputStream = new FileInputStream(FILE)) {
                Properties properties = new Properties();
                properties.load(inputStream);

                Object mainIp = properties.get("mainIp");
                Object mask = properties.get("mask");
                Object gateway = properties.get("gateway");

                if (mainIp != null) {
                    afn4F7Data.setMainIp(mainIp.toString());
                }
                if (mask != null) {
                    afn4F7Data.setMask(mask.toString());
                }
                if (gateway != null) {
                    afn4F7Data.setGateway(gateway.toString());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return afn4F7Data;
    }

}
