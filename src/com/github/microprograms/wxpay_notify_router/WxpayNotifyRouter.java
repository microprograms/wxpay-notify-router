package com.github.microprograms.wxpay_notify_router;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.microprograms.wxpay_notify_router.utils.ApiUtils;
import com.github.microprograms.wxpay_sdk_java.WXPay;
import com.github.microprograms.wxpay_sdk_java.WXPayUtil;

@WebServlet("/*")
public class WxpayNotifyRouter extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(WxpayNotifyRouter.class);
    private static final long serialVersionUID = 1L;
    private static final String qipai_exchange_app_api__wxPay_notify_api__url = "http://47.104.17.187:8083/qipai-exchange-app-api";
    private static final String qipai_exchange_app_api__wxPay_notify_api__key = "0bf86bcf-263a-4256-81a2-dcf735da5411";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final long startTimeMillis = System.currentTimeMillis();
        ServletInputStream is = request.getInputStream();
        PrintWriter writer = response.getWriter();
        String resp = "";
        Map<String, String> respMap = new HashMap<>();
        try {
            // 解析请求
            byte[] bs = new byte[request.getContentLength()];
            IOUtils.read(is, bs);
            String requestContent = new String(bs);
            log.debug("execute request, req={}", requestContent);

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(requestContent); // 转换成map
            MyWxPayConfig myWxPayConfig = new MyWxPayConfig();
            WXPay wxpay = new WXPay(myWxPayConfig);
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确，进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                JSONObject param = new JSONObject();
                param.put("data", JSON.toJSONString(notifyMap));
                param.put("key", qipai_exchange_app_api__wxPay_notify_api__key);
                String qipaiRespString = ApiUtils.post(qipai_exchange_app_api__wxPay_notify_api__url, param, myWxPayConfig.getHttpConnectTimeoutMs(), myWxPayConfig.getHttpReadTimeoutMs());
                JSONObject qipaiResp = JSON.parseObject(qipaiRespString);
                if (qipaiResp.getIntValue("code") == 0) {
                    respMap.put("return_code", "SUCCESS");
                    respMap.put("return_msg", "OK");
                } else {
                    respMap.put("return_code", "FAIL");
                    respMap.put("return_msg", qipaiResp.getString("message"));
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                respMap.put("return_code", "FAIL");
                respMap.put("return_msg", "签名失败");
            }
            resp = WXPayUtil.mapToXml(respMap);
        } catch (Throwable e) {
            log.error("err", e);
        } finally {
            // 写入响应
            writer.println(resp);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(writer);
            final long stopTimeMillis = System.currentTimeMillis();
            log.debug("over request in {}ms, resp={}", stopTimeMillis - startTimeMillis, resp);
        }
    }
}
