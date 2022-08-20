package com.license.core.configuration;

/**
 * 验证配置信息
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class LicenseConfigurationProperties {

    /**
     * 公钥
     */
    private String pubKey;

    /**
     * 验证类型
     */
    private Integer verificationType;


    /**
     * 是否开启时间强制校验,开启将记录系统时间，防止篡改本地时间
     */
    private Boolean timeForCheck;


}
