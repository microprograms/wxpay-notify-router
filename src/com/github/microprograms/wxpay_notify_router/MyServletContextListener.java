package com.github.microprograms.wxpay_notify_router;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(MyServletContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.info("contextDestroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("contextInitialized");
        log.info("SystemConfig.wXPayConfig={}", JSON.toJSONString(SystemConfig.getWXPayConfig()));
        log.info("SystemConfig.wxPay_notify_api_url={}", SystemConfig.get_wxPay_notify_api_url());
        log.info("SystemConfig.wxPay_notify_api_key={}", SystemConfig.get_wxPay_notify_api_key());
    }

}
