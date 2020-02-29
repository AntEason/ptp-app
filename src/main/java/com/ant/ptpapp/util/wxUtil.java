package com.ant.ptpapp.util;

import org.apache.ws.security.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 2:28 上午
 */
public class wxUtil {
    /**
     * 解密用户手机号
     * @param keyStr sessionkey
     * @param ivStr  ivData
     * @param encDataStr 带解密数据
     * @return
     * @throws Exception
     * @date 2019年05月08日
     */
    public static String decrypt(String keyStr, String ivStr, String encDataStr)throws Exception {

        byte[] encData = Base64.decode(encDataStr);
        byte[] iv =Base64.decode(ivStr);
        byte[] key = Base64.decode(keyStr);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return new String(cipher.doFinal(encData),"UTF-8");
    }
}
