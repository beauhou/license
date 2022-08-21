package com.beauhou.license.core.handler;

import com.beauhou.license.core.factory.LicenseFactory;
import com.beauhou.license.core.utils.LicenseUtils;
import com.beauhou.license.core.mode.AuthorPartInfo;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 处理签名
 *
 * @author: BeauHou
 * @Date: 2022/8/21
 * @Description:
 */
public class SignatureHandler {

    private static final String signatureKey = "signatureKey";

    /**
     * 生成签名
     *
     * @param authorPartInfo
     * @return
     */
    public static String signature(AuthorPartInfo authorPartInfo) {
        Field[] declaredFields = authorPartInfo.getClass().getDeclaredFields();
        StringBuffer stringBuffer = new StringBuffer();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object o = null;
            try {
                o = declaredField.get(authorPartInfo);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (o != null) {
                stringBuffer.append(declaredField + "=" + o + "&");
            }
        }
        stringBuffer.append(signatureKey + "=" + LicenseFactory.getSignatureKey());
        return LicenseUtils.getSHA256(stringBuffer.toString());
    }

    /**
     * 生成签名
     *
     * @return
     */
    public static String signature() {
        AuthorPartInfo currentAuthorPartInfo = LicenseFactory.getCurrentAuthorPartInfo();
        Optional.ofNullable(currentAuthorPartInfo).orElseThrow(() -> new RuntimeException("AuthorPartInfo为空"));
        return signature(currentAuthorPartInfo);
    }
}
