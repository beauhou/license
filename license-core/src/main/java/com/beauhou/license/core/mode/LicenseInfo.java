package com.beauhou.license.core.mode;

/**
 * 验证模型模型
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */

public class LicenseInfo {

    /**
     * 公司信息
     */
    private AuthorPartInfo authorPartInfo;

    /**
     * 签名
     */
    private String signature;


    /**
     * 版权
     */
    private String copyright;


    public AuthorPartInfo getAuthorPartInfo() {
        return authorPartInfo;
    }

    public void setAuthorPartInfo(AuthorPartInfo authorPartInfo) {
        this.authorPartInfo = authorPartInfo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
