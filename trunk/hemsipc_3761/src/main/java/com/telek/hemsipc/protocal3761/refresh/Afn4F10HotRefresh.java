package com.telek.hemsipc.protocal3761.refresh;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.datamodel.Afn4F10Data;
import com.telek.hemsipc.protocal3761.protocal.constant.ProcotolTypeConst;
import com.telek.hemsipc.protocal3761.protocal.constant.SpeedConst;
import com.telek.hemsipc.protocal3761.service.request.SetAfn4F10Params;

public class Afn4F10HotRefresh extends AbsHotRefresh  {

    private static Logger log = LoggerFactory.getLogger(Afn4F10HotRefresh.class);

    private static final String SPLIT = ",";
    public static final String FILE_NAME = "afn4f10.properties";
    public static final String FILE_PATH = ROOT_PATH + "/" + FILE_NAME;
    public static final File FILE = new File(FILE_PATH);

//    public static void refresh() {
//        try {
//            String terminalAddress = getTerminalAddress(FILE);
//            String address = null;
//            for (Map.Entry<String, String> entry : Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.entrySet()) {
//                String eTerminalAddress = entry.getValue();
//                if (eTerminalAddress.equalsIgnoreCase(terminalAddress)) {
//                    address = entry.getKey();
//                    break;
//                }
//            }
//            if (address != null) {
//                List<Afn4F10Data> datas = getDatas();
//                SetAfn4F10Params setAfn4F10Params = new SetAfn4F10Params(true, datas);
//                setAfn4F10Params.dealSendToTerminal(address, null);
//            } else {
//                log.error("未登录或离线：" + terminalAddress);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static List<Afn4F10Data> getDatas() {
        List<Afn4F10Data> list = new ArrayList<>();

        if (FILE != null && FILE.exists()) {
            try (InputStream inputStream = new FileInputStream(FILE)) {
                Properties properties = new Properties();
                properties.load(inputStream);

                String[] protectorIndexs = properties.get("protectorIndex").toString().split(SPLIT);
                String[] measuringPoints = properties.get("measuringPoint").toString().split(SPLIT);
                String[] ports = properties.get("port").toString().split(SPLIT);
                String[] speeds = properties.get("speed").toString().split(SPLIT);
                String[] protocalTypes = properties.get("protocalType").toString().split(SPLIT);
                String[] addresss = properties.get("address").toString().split(SPLIT);
                String[] passwords = properties.get("password").toString().split(SPLIT);

                int length = protectorIndexs.length;
                if (length == measuringPoints.length && protectorIndexs.length == ports.length && length == speeds.length
                        && length == protocalTypes.length && length == addresss.length && length == passwords.length) {
                    for (int i = 0; i < length; i++) {
                        String protectorIndex = protectorIndexs[i];
                        String measuringPoint = measuringPoints[i];
                        String port = ports[i];
                        String speed = speeds[i];
                        String protocalType = protocalTypes[i];
                        String address = addresss[i];
                        String password = passwords[i];
                        Afn4F10Data afn4F10Data = new Afn4F10Data();
                        afn4F10Data.setProtectorIndex(Integer.parseInt(protectorIndex));
                        afn4F10Data.setMeasuringPoint(Integer.parseInt(measuringPoint));
                        afn4F10Data.setPort(Integer.parseInt(port));
                        afn4F10Data.setSpeed(SpeedConst.getByCode(Integer.parseInt(speed)));
                        afn4F10Data.setProtocalType(ProcotolTypeConst.getByCode(Integer.parseInt(protocalType)));
                        afn4F10Data.setAddress(address);
                        afn4F10Data.setPassword(password);
                        list.add(afn4F10Data);
                    }
                } else {
                    log.error("热更新中字段个数不相等");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return list;
    }

}
