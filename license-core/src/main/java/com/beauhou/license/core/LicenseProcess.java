package com.beauhou.license.core;

import com.beauhou.license.core.configuration.LicenseProperties;
import com.beauhou.license.core.verification.CoreVerification;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author: BeauHou
 * @Date: 2022/8/20
 * @Description:
 */
public class LicenseProcess {

    private static Logger logger = Logger.getLogger("LicenseProcess");

    /**
     * 调度线程池
     */
    private static ScheduledExecutorService scheduled = new ScheduledThreadPoolExecutor(1);

    /**
     * 验证启动地址
     *
     * @param licenseProperties 配置信息
     */
    public static void start(LicenseProperties licenseProperties) {

        CoreVerification coreVerification = new CoreVerification();
        if (!coreVerification.verifyLicense(licenseProperties, true)) {
            logger.warning("授权信息不正确将退出");
            System.exit(0);
        }
        scheduled.scheduleAtFixedRate(() -> {
            if (!coreVerification.verifyLicense(licenseProperties, false)) {
                logger.warning("授权信息不正确将退出");
                System.exit(0);
            }
        }, 1, 1, TimeUnit.DAYS);
    }
}
