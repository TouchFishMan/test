package com.telek.hemsipc.protocal3761;

import java.util.HashMap;
import java.util.Map;

import com.telek.hemsipc.protocal3761.protocal.IProtocal;
import com.telek.hemsipc.protocal3761.protocal.ProtocalManagerFactory;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalImpl;

public class ProtocalFactory {

    /**
     * 主站作为主动站和从动站, 每一个终端对应不同的解码器
     * 因为每一个主动站和从动站之间有一个唯一的seq
     * 主站作为主动站: seq从1开始
     * 主站作为从动站, seq从第一次接收终端的seq开始
     *
     * key: address+prm  (prm:1-启动站, 0-从动站)-------------以主站为基准
     */
    public static Map<String, IProtocal> addressProtocalCache = new HashMap<>();

    /**
     * 获取编解码器
     * @param address
     * @param isStartStation 是否为启动站
     * @return
     */
    public static IProtocal getProtocal(String address, Boolean isStartStation) {
        try {
            IProtocal protocal;
            if (address == null) {
                protocal = ProtocalManagerFactory.getProtocalManager("rgm_376_1");
            } else {
                int prm;
                if (isStartStation) {
                    prm = CommConst.PRM_START_STATION;
                } else {
                    prm = CommConst.PRM_SLAVE_STATION;
                }
                String key = address + prm;
                protocal = addressProtocalCache.get(key);
                if (protocal == null) {
                    protocal = ProtocalManagerFactory.getProtocalManager("rgm_376_1");
                    ProtocalImpl impl = (ProtocalImpl) protocal;
                    impl.setStartStation(isStartStation);
                    addressProtocalCache.put(key, protocal);
                }
            }
            return protocal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
