package com.github.microprograms.wxpay_notify_router;

import java.io.InputStream;

import com.github.microprograms.wxpay_sdk_java.WXPayConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SystemConfig {
    public static final int httpConnectTimeoutMs = 8000;
    public static final int httpReadTimeoutMs = 10000;
    private static Config config;

    static {
        config = ConfigFactory.load();
    }

    public static WXPayConfig getWXPayConfig() {
        return new WXPayConfig() {

            @Override
            public String getMchID() {
                return null;
            }

            @Override
            public String getKey() {
                return config.getString("wxPay_key");
            }

            @Override
            public int getHttpConnectTimeoutMs() {
                return httpConnectTimeoutMs;
            }

            @Override
            public int getHttpReadTimeoutMs() {
                return httpReadTimeoutMs;
            }

            @Override
            public InputStream getCertStream() {
                return null;
            }

            @Override
            public String getAppID() {
                return null;
            }
        };
    }

    public static String get_wxPay_notify_api_name() {
        return config.getString("wxPay_notify_api_name");
    }

    public static String get_wxPay_notify_api_url() {
        return config.getString("wxPay_notify_api_url");
    }

    public static String get_wxPay_notify_api_key() {
        return config.getString("wxPay_notify_api_key");
    }
}
