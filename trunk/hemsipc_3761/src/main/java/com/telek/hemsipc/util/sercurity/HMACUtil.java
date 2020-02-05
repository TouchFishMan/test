package com.telek.hemsipc.util.sercurity;

import com.telek.hemsipc.sdmp.OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * Description:生成摘要码工具类 HMACUtil.java Create on 2012-10-11 下午2:56:03
 * 
 * @author jxj
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public class HMACUtil {
    private static Logger LOG = LoggerFactory.getLogger(HMACUtil.class);

    /**
     * Description:生成HMAC摘要码 Date:2012-10-11
     * 
     * @author jxj
     * @param data
     * @param key 密钥
     * @throws Exception
     * @return String
     */
    public static byte[] encryptHMAC(byte[] data, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (Exception e) {
            LOG.error("encryptHMAC error " + e.getMessage());
            return null;
        }
    }

    /**
     * Description: Date:2014-12-2 摘要码验证
     * 
     * @author shenbf
     * @param @param msgAuthenticationParameters
     * @param @param scorpPDU
     * @param @param key
     * @param @return
     * @return boolean
     */
    public static boolean HMACIsValid(OctetString msgAuthenticationParameters, byte[] scorpPDU, byte[] key) {
        try {
            byte[] hmac = HMACUtil.encryptHMAC(scorpPDU, key);
            if (!Arrays.equals(hmac, msgAuthenticationParameters.getValue())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
