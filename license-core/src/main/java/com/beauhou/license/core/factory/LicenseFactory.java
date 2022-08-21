package com.beauhou.license.core.factory;

import com.beauhou.license.core.mode.AuthorPartInfo;

/**
 * license构建工厂
 *
 * @author: BeauHou
 * @Date: 2022/8/21
 * @Description:
 */
public class LicenseFactory {

    /**
     * 工厂对象
     */
    private static LicenseFactory licenseFactory;

    /**
     * license模型
     */
    private static AuthorPartInfo authorPartInfo;

    /**
     * 签名秘钥
     */
    private static String signatureKey;

    private LicenseFactory() {

    }

    /**
     * 构建license单例
     *
     * @return
     */
    public static LicenseFactory build(AuthorPartInfo authorPartInfo, String signatureKey) {
        if (licenseFactory == null) {
            licenseFactory = new LicenseFactory();
            LicenseFactory.authorPartInfo = authorPartInfo;
            LicenseFactory.signatureKey = signatureKey;
        }
        return licenseFactory;
    }


    /**
     * 获取当前license对象
     *
     * @return
     */
    public static AuthorPartInfo getCurrentAuthorPartInfo() {
        return LicenseFactory.authorPartInfo;
    }

    /**
     * 获取签名秘钥
     *
     * @return
     */
    public static String getSignatureKey() {
        return LicenseFactory.signatureKey;
    }

}
