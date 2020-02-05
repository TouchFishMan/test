package com.telek.hemsipc.protocol.dl645;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.model.Dl645ReadingProtocol;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @Auther: wll
 * @Date: 2018/9/13 16:54
 * @Description: 数据解码类
 */
@Component
public class ReadDataDecode {
    private Logger log = LoggerFactory.getLogger(ReadDataDecode.class);
    public static final Integer DECODE_TYPE_FLOAT = 1;
    public static final Integer DECODE_TYPE_FLOAT_BLOCK = 2;

    public String decodeData(byte[] dataid, byte[] data, Dl645ReadingProtocol readingProtocol) {
        if (readingProtocol.getDecodeType() == DECODE_TYPE_FLOAT) {
            return decodeFloat(data, readingProtocol.getDataPattern(), readingProtocol.getDataLength(), readingProtocol.isFirstIsPostive());
        } else if (readingProtocol.getDecodeType() == DECODE_TYPE_FLOAT_BLOCK) {
            String[] blockDatas = readingProtocol.getBlockDatas().split(",");
            String[] dataResults = decodeFloatBlock(data, readingProtocol.getDataPattern(), readingProtocol.getDataLength(), blockDatas.length, readingProtocol.isFirstIsPostive());
            return StringUtils.join(dataResults, ",");
        }
        return null;
    }

//    public Vector<VarBind> decodeData(byte[] dataid, byte[] data, Dl645ReadingProtocol readingProtocol) {
//        SdmpProtocol sdmpProtocol = DeviceContext.sdmpProtocolMap.get(readingProtocol.getSdmpProtocolKey());
//        if (readingProtocol == null || sdmpProtocol == null) {
//            log.error("【read解码】协议对象为空，readingProtocol：{}，sdmpProtocol:{}", readingProtocol, sdmpProtocol);
//            throw new RuntimeException();
//        }
//        Vector<VarBind> binds = new Vector<>();
//        if (readingProtocol.getDecodeType() == DECODE_TYPE_FLOAT) {
//            String dataResult = decodeFloat(data, readingProtocol.getDataPattern(), readingProtocol.getDataLength(), readingProtocol.isFirstIsPostive());
//            if (sdmpProtocol.getProtocolDataType() == 1) {
//                binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new Integer32((int) (Float.parseFloat(dataResult) * sdmpProtocol.getProtocolMultiplier() - sdmpProtocol.getValueOffset()))));
//            } else if (sdmpProtocol.getProtocolDataType() == 2) {
//                binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new OctetString(dataResult)));
//            } else {
//                log.error("【read解码】不支持该sdmp协议，{}", sdmpProtocol);
//                throw new RuntimeException();
//            }
//        } else if (readingProtocol.getDecodeType() == DECODE_TYPE_FLOAT_BLOCK) {
//            String[] blockDatas = readingProtocol.getBlockDatas().split(",");
//            String[] dataResults = decodeFloatBlock(data, readingProtocol.getDataPattern(), readingProtocol.getDataLength(), blockDatas.length, readingProtocol.isFirstIsPostive());
//            for (int i = 0; i < blockDatas.length; i++) {
//                Dl645ReadingProtocol readingProtocolBlock = DeviceContext.dl645ReadingProtocolMap.get(blockDatas[i]);
//                SdmpProtocol SdmpProtocolBlock = DeviceContext.sdmpProtocolMap.get(readingProtocolBlock.getSdmpProtocolKey());
//                if (SdmpProtocolBlock.getProtocolDataType() == 1) {
//                    binds.add(new VarBind(SdmpProtocolBlock.getProtocolKey(), new Integer32((int) (Float.parseFloat(dataResults[i]) * SdmpProtocolBlock.getProtocolMultiplier() - SdmpProtocolBlock.getValueOffset()))));
//                } else if (sdmpProtocol.getProtocolDataType() == 2) {
//                    binds.add(new VarBind(sdmpProtocol.getProtocolKey(), new OctetString(dataResults[i])));
//                } else {
//                    log.error("【read解码】不支持该sdmp协议，{}", sdmpProtocol);
//                    throw new RuntimeException();
//                }
//            }
//        }
//        return binds;
//    }

    /**
     * @Description: 解码float类型的数据
     * @auther: wll
     * @date: 16:57 2018/9/13
     * @param: [data, pattern, length, isPostive]
     * @return: void
     */
    private String decodeFloat(byte[] data, String pattern, int length, boolean firstIsPostive) {
        //正则匹配,判断去除小数点后剩下的字符串数量是否和数据长度匹配，判断数据和数据长度为是否一致
        if (!Pattern.matches("X+\\.X+", pattern) || !(pattern.length() == data.length * 2 + 1)) {
            log.error("【read解码】pattern有误，{}", pattern);
            throw new RuntimeException();
        }
        //小数点位置
        int pointIndex = pattern.indexOf(".");
        StringBuffer result = new StringBuffer();
        if (firstIsPostive) {
            //最高位符号位代表正负标志，0正1负
            int postive = (data[0] & 0x80) >> 7;
            if (postive == 0x00) {
                result.append(HexBin.encode(data));
                result.insert(pointIndex, ".");
            } else if (postive == 0x01) {
                result.append("-");
                data[0] = (byte) (data[0] & 0x7F);
                result.append(HexBin.encode(data));
                result.insert(pointIndex + 1, ".");
            }
        } else {
            result.append(HexBin.encode(data));
            result.insert(pointIndex, ".");
        }
        return result.toString();
    }

    /**
     * @Description: 解码float数据块
     * @auther: wll
     * @date: 9:58 2018/9/14
     * @param: [data, pattern, length, firstIsPostive]
     * @return: void
     */
    private String[] decodeFloatBlock(byte[] data, String pattern, int length, int blockNum, boolean firstIsPostive) {
        if (data.length != length * blockNum) {
            log.error("【read-block解码】data有误，{}", HexBin.encode(data));
            throw new RuntimeException();
        }
        String[] result = new String[blockNum];
        for (int i = 0; i < blockNum; i++) {
            byte[] blockData = new byte[length];
            System.arraycopy(data, length * i, blockData, 0, length);
            result[i] = decodeFloat(blockData, pattern, length, firstIsPostive);
        }
        return result;
    }
}
