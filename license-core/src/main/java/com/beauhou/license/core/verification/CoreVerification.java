package com.beauhou.license.core.verification;

import com.beauhou.license.core.configuration.LicenseProperties;
import com.beauhou.license.core.encrypt.RsaEncrypt;
import com.beauhou.license.core.factory.LicenseFactory;
import com.beauhou.license.core.handler.SignatureHandler;
import com.beauhou.license.core.utils.HardwareInformUtils;
import com.beauhou.license.core.mode.AuthorPartInfo;
import com.beauhou.license.core.mode.LicenseInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * 核心验证服务
 *
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class CoreVerification {

    private Logger logger = Logger.getLogger("CoreVerification");

    /**
     * 验证授权码
     *
     * @param licenseProperties 配置信息
     * @return
     * @throws Exception
     */
    public boolean verifyLicense(LicenseProperties licenseProperties, boolean isFirst) {
        LicenseInfo licenseInfo = null;
        try {
            licenseInfo = RsaEncrypt.decrypt(licenseProperties.getLicensePath(), licenseProperties.getPubKeyPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (isFirst) {
            logger.info(String.format("%s,欢迎使用", licenseInfo.getAuthorPartInfo().getName()));
            logger.info(String.format("copyright %s", licenseInfo.getCopyright()));
            if (licenseInfo.getAuthorPartInfo().getLongTerm()) {
                logger.info(String.format("到期时间： 永久", licenseInfo.getCopyright()));
            } else {
                logger.info(String.format("到期时间： %s", licenseInfo.getAuthorPartInfo().getExpireTime()));
            }
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
     * 验证其他信息
     *
     * @return true验证成功  false-验证失败
     */
    private boolean verifyHardwareInfo() {
        AuthorPartInfo authorPartInfo = LicenseFactory.getCurrentAuthorPartInfo();
        List<String> localIPS = HardwareInformUtils.getLocalIPS();
        if (!localIPS.contains(authorPartInfo.getIp())) {
            logger.warning("ip信息不正确");
            return false;
        }
        //验证有效期
        if (!authorPartInfo.getLongTerm()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parseDate = simpleDateFormat.parse(authorPartInfo.getExpireTime());
                //授权验证码即将过期
                if (new Date().compareTo(parseDate) < 0) {
                    logger.warning("有效期已过");
                    return false;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
