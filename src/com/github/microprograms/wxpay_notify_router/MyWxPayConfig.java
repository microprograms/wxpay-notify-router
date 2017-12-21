package com.github.microprograms.wxpay_notify_router;

import java.io.InputStream;

import com.github.microprograms.wxpay_sdk_java.WXPayConfig;

public class MyWxPayConfig implements WXPayConfig {

    public String getAppID() {
        return "wx0d872f6996ff0b83";
    }

    public String getMchID() {
        return "1494498622";
    }

    public String getKey() {
        return "aded8b9ac7c7480bafca795363264f2a";
    }

    public InputStream getCertStream() {
        return null;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
