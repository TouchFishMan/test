package com.telek.hemsipc.protocal3761.refresh;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.service.request.GetRequest;

public class GetF7HotRefresh extends AbsHotRefresh  {

    private static Logger log = LoggerFactory.getLogger(GetF7HotRefresh.class);

    public static final String FILE_NAME = "get.properties";
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
                GetRequest setAfn4F10Params = new GetRequest(true);
                setAfn4F10Params.dealSendToTerminal(address2, null);
            } else {
                log.error("未登录或离线：" + terminalAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
