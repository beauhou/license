package com.beauhou.license.core.mode;

/**
 * @author: BeauHou
 * @Date: 2022/8/21
 * @Description:
 */
public class AuthorPartInfo {

    /**
     * 公司名称
     */
    private String name;

    /**
     * mac地址
     */
    private String mac;

    /**
     * ip
     */
    private String ip;

    /**
     * 硬盘
     */
    private String disk;

    /**
     * 是否长久
     */
    private Boolean isLongTerm = true;


    /**
     * 到期时间
     */
    private String expireTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public Boolean getLongTerm() {
        return isLongTerm;
    }

    public void setLongTerm(Boolean longTerm) {
        isLongTerm = longTerm;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
