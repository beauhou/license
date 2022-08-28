package com.beauhou.license.register;

import com.alibaba.fastjson.JSON;
import com.beauhou.license.core.encrypt.RsaEncrypt;
import com.beauhou.license.core.factory.LicenseFactory;
import com.beauhou.license.core.handler.SignatureHandler;
import com.beauhou.license.core.mode.AuthorPartInfo;
import com.beauhou.license.core.mode.LicenseInfo;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author: BeauHou
 * @Date: 2022/8/21
 * @Description:
 */
public class StartRegister {

    public static void main(String[] args) throws Exception {
        String path = StartRegister.class.getClassLoader().getResource("PriKey.key").getPath();
        LicenseInfo licenseInfo = generationLicenseInfo();
        String license = RsaEncrypt.encrypt(JSON.toJSONString(licenseInfo), path);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(String.format("%s.lic", licenseInfo
                .getAuthorPartInfo().getName())));
        osw.write(license);
        osw.flush();
        osw.close();
    }


    /**
     * 生成授权信息
     *
     * @return
     */

    private static LicenseInfo generationLicenseInfo() {
        AuthorPartInfo authorPartInfo = new AuthorPartInfo();
        authorPartInfo.setName("beauhou");
        authorPartInfo.setIp("192.168.3.190");
        LicenseFactory.build(authorPartInfo, "beauhou");
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setAuthorPartInfo(authorPartInfo);
        licenseInfo.setSignature(SignatureHandler.signature());
        licenseInfo.setCopyright("beauhou");
        return licenseInfo;
    }
}
