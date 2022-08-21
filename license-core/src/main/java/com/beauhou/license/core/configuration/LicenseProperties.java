package com.beauhou.license.core.configuration;

/**
 * 验证配置信息
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class LicenseProperties {

    /**
     * 公钥地址
     */
    private String pubKeyPath;

    /**
     * license地址
     */
    private String licensePath;

    /**
     * 签名秘钥
     */
    private String signatureKey;


    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }
}
