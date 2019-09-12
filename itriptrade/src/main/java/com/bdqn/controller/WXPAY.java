package com.bdqn.controller;

import com.bdqn.Pay.WXPayConfig;
import com.bdqn.util.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WXPAY {

    @Resource
    WXPayConfig payConfig;

    @RequestMapping(value = "/Pay1", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getpay(String OrderiD) throws Exception {
        WXPayRequest request = new WXPayRequest(payConfig);
        Map<String, String> map = new HashMap<>();
        map.put("body", "测试商品");
        map.put("out_trade_no", OrderiD);
        map.put("total_fee", "1");
        // 这个IP地址是课工厂提供的和使用的账号一起申请使用的。
        map.put("spbill_create_ip", "47.92.146.135");
        map.put("notify_url", "xiazhao505.vicp.io/trade/Pay2");
        map.put("device_info", "");
        //微信返回的结构
        Map<String, String> result = request.unifiedorder(map);
        //方法返回的map
        Map<String, String> result1 = new HashMap<>();
        if (result.get("return_code").equals("SUCCESS")) {
            result1.put("url", result.get("code_url"));
        }
        return result1;
    }


    //接收的回调函数必须是post 方式
    @RequestMapping(value = "/Pay2", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getpay(HttpServletRequest request) {
        WXPayRequest wxPayRequest = new WXPayRequest(this.payConfig);
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> params = null;
        try {
            InputStream inputStream;
            StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            params = WXPayUtil.xmlToMap(sb.toString());
            boolean flag = wxPayRequest.isResponseSignatureValid(params);
            if (flag) {
                String returnCode = params.get("return_code");
                if (returnCode.equals("SUCCESS")) {


                    String transactionId = params.get("transaction_id");
                    String outTradeNo = params.get("out_trade_no");
                    //业务操作

                    //修改数据库中状态

                } else {
                    result.put("return_code", "FAIL");
                    result.put("return_msg", "支付失败");
                }
            } else {
                result.put("return_code", "FAIL");
                result.put("return_msg", "签名失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  "回调成功";

    }
}
