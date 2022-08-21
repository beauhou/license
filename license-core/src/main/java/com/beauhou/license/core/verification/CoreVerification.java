package com.beauhou.license.core.verification;

import com.beauhou.license.core.configuration.LicenseProperties;
import com.beauhou.license.core.encrypt.RsaEncrypt;
import com.beauhou.license.core.factory.LicenseFactory;
import com.beauhou.license.core.handler.SignatureHandler;
import com.beauhou.license.core.utils.HardwareInformUtils;
import com.beauhou.license.core.mode.AuthorPartInfo;
import com.beauhou.license.core.mode.LicenseInfo;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 核心验证服务
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class CoreVerification {


    private static ScheduledExecutorService scheduled = new ScheduledThreadPoolExecutor(1);

    /**
     * 验证授权码
     *
     * @param licenseProperties 配置信息
     * @return
     * @throws Exception
     */
    public boolean verifyLicense(LicenseProperties licenseProperties) throws Exception {
        LicenseInfo licenseInfo = RsaEncrypt.decrypt(licenseProperties.getLicensePath(), licenseProperties.getPubKeyPath());
        System.out.println(String.format("%s,欢迎使用", licenseInfo.getAuthorPartInfo().getName()));
        System.out.println(String.format("copyright %s", licenseInfo.getCopyright()));
        if (licenseInfo.getAuthorPartInfo().getLongTerm()) {
            System.out.println(String.format("到期时间： 永久", licenseInfo.getCopyright()));
        } else {
            System.out.println(String.format("到期时间： %s", licenseInfo.getAuthorPartInfo().getExpireTime()));
        }
        LicenseFactory.build(licenseInfo.getAuthorPartInfo(), licenseProperties.getSignatureKey());
        if (!verifySignature(licenseInfo)) {
            return false;
        }
        return verifyHardwareInfo();
    }


    /**
     * 验证签名是否正确
     *
     * @return true-验证成功  false-验证是失败
     */
    private boolean verifySignature(LicenseInfo licenseInfo) {

        return licenseInfo.getSignature().equals(SignatureHandler.signature());
    }

    /**
     * 验证硬件信息是否匹配
     *
     * @return true验证成功  false-验证失败
     */
    private boolean verifyHardwareInfo() {
        AuthorPartInfo authorPartInfo = LicenseFactory.getCurrentAuthorPartInfo();
        List<String> localIPS = HardwareInformUtils.getLocalIPS();
        if (!localIPS.contains(authorPartInfo.getIp())) {
            System.out.println("ip信息不正确");
            return false;
        }
        return true;
    }

}
