package com.meeting.modules.magazine.controller;

import com.meeting.modules.magazine.config.WeChatConfig;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import com.meeting.modules.magazine.mapper.JournalOrderMapper;
import com.meeting.modules.magazine.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * 微信支付相关类
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-04-18
 */
@RestController
@RequestMapping("/api/v1/wechat")
public class WeChatController {

    private final WeChatConfig weChatConfig;

    private final JournalOrderMapper journalOrderMapper;

    @Autowired
    public WeChatController(WeChatConfig weChatConfig, JournalOrderMapper journalOrderMapper) {
        this.weChatConfig = weChatConfig;
        this.journalOrderMapper = journalOrderMapper;
    }

    /**
     * 微信支付回调
     * @param request
     * @param response
     */
    @RequestMapping("/order/callback")
    public void orderCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception{

        InputStream inputStream = request.getInputStream();
        // BufferedReader是包装设计模式，性能比较高
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null){
            sb.append(line);
        }
        in.close();
        inputStream.close();
        Map<String,String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callbackMap.toString());

        SortedMap<String,String> sortedMap = WXPayUtil.getSortedMap(callbackMap);

        // 判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap,weChatConfig.getKey())){
            if ("SUCCESS".equals(sortedMap.get("return_code"))){
                // 根据流水号查询信息
                String outTradeNo = sortedMap.get("out_trade_no");
                JournalOrderEntity outTradeNoEntity = journalOrderMapper.findByOutTradeNo(outTradeNo);
                // 更新订单状态
                if ( outTradeNoEntity != null && outTradeNoEntity.getState() == 0 ){
                    JournalOrderEntity journalOrderEntity = new JournalOrderEntity();
                    journalOrderEntity.setOpenid(sortedMap.get("appid"));
                    journalOrderEntity.setOutTradeNo(sortedMap.get("out_trade_no"));
                    journalOrderEntity.setNotifyTime(new Date());
                    journalOrderEntity.setState(1);
                    int rows = journalOrderMapper.updateJournalOrderByOutTradeNo(journalOrderEntity);
                    // 影响行数row == 1 或者 row == 0 响应微信成功或者失败
                    if ( rows == 1 ){
                        // 通知微信订单处理成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }
        // 其他情况都处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
    }

}
