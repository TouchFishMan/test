package com.telek.hemsipc.util.sercurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.Key;

public class AESUtil {
    private static Logger LOG = LoggerFactory.getLogger(AESUtil.class);
    private static String AES = "AES";

    /**
     * Description:对字符数组进行加密 Date:2012-10-11
     *
     * @param str
     * @param key 密钥
     * @return byte[]
     * @throws Exception
     * @author jxj
     */
    public static byte[] encrypt(byte[] str, byte[] key) {
        Key skey = new javax.crypto.spec.SecretKeySpec(key, AES);
        Cipher encryptCipher;
        try {
            encryptCipher = Cipher.getInstance(AES);
            encryptCipher.init(Cipher.ENCRYPT_MODE, skey);
            return encryptCipher.doFinal(str);
        } catch (Exception e) {
            LOG.error("encrypt error: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Description:对字节数组进行解密 Date:2012-10-11
     *
     * @param str
     * @param key 密钥
     * @return byte[]
     * @throws Exception
     * @author jxj
     */
    public static byte[] decrypt(byte[] str, byte[] key) {
        Key skey = new javax.crypto.spec.SecretKeySpec(key, AES);
        Cipher decryptCipher;
        try {
            decryptCipher = Cipher.getInstance(AES);
            decryptCipher.init(Cipher.DECRYPT_MODE, skey);
            return decryptCipher.doFinal(str);
        } catch (Exception e) {
            LOG.error("decrypt error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Description:根据需加密byte数组长度获取加密后byte数组长度 Date:2012-10-15
     *
     * @param inputLen 需加密byte数组长度
     * @param key      密钥
     * @return int 加密后byte数组长度
     * @throws Exception
     * @author jxj
     */
    public static int getEncryptLength(int inputLen, byte[] key) {
        Key skey = new javax.crypto.spec.SecretKeySpec(key, AES);
        Cipher encryptCipher;
        try {
            encryptCipher = Cipher.getInstance(AES);
            encryptCipher.init(Cipher.ENCRYPT_MODE, skey);
            return encryptCipher.getOutputSize(inputLen);
        } catch (Exception e) {
            return -1;
        }
    }
}
