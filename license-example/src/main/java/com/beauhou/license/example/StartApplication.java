package com.beauhou.license.example;

import com.beauhou.license.core.LicenseProcess;
import com.beauhou.license.core.configuration.LicenseProperties;

/**
 * @author: BeauHou
 * @Date: 2022/8/21
 * @Description:
 */
public class StartApplication {
    public static void main(String[] args) {
        LicenseProperties licenseProperties = new LicenseProperties();
        licenseProperties.setLicensePath(StartApplication.class.getClassLoader().getResource("beauhou.lic").getPath());
        licenseProperties.setPubKeyPath(StartApplication.class.getClassLoader().getResource("PubKey.key").getPath());
        licenseProperties.setSignatureKey("beauhou");
        LicenseProcess.start(licenseProperties);
    }
}
