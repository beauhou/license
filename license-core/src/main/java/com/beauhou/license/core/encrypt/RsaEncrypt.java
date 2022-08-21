package com.beauhou.license.core.encrypt;

import com.alibaba.fastjson.JSON;
import com.beauhou.license.core.mode.LicenseInfo;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * RSA加密
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class RsaEncrypt {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 私钥加密
     *
     * @param str
     * @param privateKeyPath 私钥路径
     * @return license
     * @throws Exception
     */
    public static String encrypt(String str, String privateKeyPath) throws Exception {
        String privateKey = getKeyByPath(privateKeyPath);
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        // 分段加密
        // URLEncoder编码解决中文乱码问题
        byte[] data = URLEncoder.encode(str, "UTF-8").getBytes("UTF-8");
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        String outStr = Base64.getEncoder().encodeToString(enBytes);
        return outStr;
    }


    /**
     * 公钥分段解密
     *
     * @param licensePath   许可证地址
     * @param publicKeyPath 公钥路径
     * @return 解析后对象
     * @throws Exception
     */
    public static LicenseInfo decrypt(String licensePath, String publicKeyPath) throws Exception {
        String publicKey = getKeyByPath(publicKeyPath);
        String licenseStr = getKeyByPath(licensePath);
        // 获取公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        byte[] data = Base64.getDecoder().decode(licenseStr.getBytes("UTF-8"));
        // 返回UTF-8编码的解密信息
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String decode = URLDecoder.decode(new String(decryptedData, "UTF-8"), "UTF-8");
        return JSON.parseObject(decode, LicenseInfo.class);
    }


    /**
     * 获取私钥
     *
     * @param keyPath 秘钥地址
     * @return 秘钥内容
     */
    public static String getKeyByPath(String keyPath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(keyPath);
        StringBuffer stringBuffer = new StringBuffer();
        int len = 0;
        byte[] bytes = new byte[1024 * 1000];
        while ((len = fileInputStream.read(bytes)) != -1) {
            stringBuffer.append(new String(bytes, 0, len));
        }
        return stringBuffer.toString();
    }
}
