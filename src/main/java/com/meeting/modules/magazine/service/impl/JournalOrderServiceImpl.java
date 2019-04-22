package com.meeting.modules.magazine.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.Query;
import com.meeting.modules.magazine.config.WeChatConfig;
import com.meeting.modules.magazine.constant.MagazineConstant;
import com.meeting.modules.magazine.entity.JournalEntity;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import com.meeting.modules.magazine.mapper.JournalOrderMapper;
import com.meeting.modules.magazine.service.JournalOrderService;
import com.meeting.modules.magazine.service.JournalService;
import com.meeting.modules.magazine.utils.CommonUtils;
import com.meeting.modules.magazine.utils.HttpUtils;
import com.meeting.modules.magazine.utils.IpUtils;
import com.meeting.modules.magazine.utils.WXPayUtil;
import com.meeting.modules.magazine.vo.JournalOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * 杂志订阅表
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-04-10
 */
@Service
public class JournalOrderServiceImpl extends ServiceImpl<JournalOrderMapper, JournalOrderEntity> implements JournalOrderService {

    private final JournalService journalService;

    private final JournalOrderMapper orderMapper;

    private final WeChatConfig weChatConfig;

    @Autowired
    public JournalOrderServiceImpl(JournalService journalService, JournalOrderMapper orderMapper, WeChatConfig weChatConfig) {
        this.journalService = journalService;
        this.orderMapper = orderMapper;
        this.weChatConfig = weChatConfig;
    }

    @Override
    public Boolean saveOrder(Long journalId, HttpServletRequest request) {
        JournalOrderEntity entity = new JournalOrderEntity();
//        String ip = IpUtils.getIpAddr(request);
//        Long userId = Long.valueOf(String.valueOf(request.getAttribute("user_id")));
        Long userId = 3L;
        String ip = "39.171.12.6";
        entity.setIp(ip);
        entity.setOutTradeNo(CommonUtils.generateUUID());
        entity.setCreateTime(new Date());
        entity.setJournalId(journalId);
        // 查询当前杂志相关信息
        JournalEntity journalEntity = journalService.selectById(journalId);
        entity.setTotalFee(journalEntity.getPrice());
        entity.setJournalTitle(journalEntity.getTitle());
        entity.setJournalImg(journalEntity.getCoverImg());
        // todo 支付状态后期v1.2开发
        entity.setState(MagazineConstant.FALSE);
        entity.setUserId(userId);
        return this.insert(entity);
    }

    @Override
    public PageUtils<JournalOrderEntity> queryByUserId(Map<String, Object> params, Long userId) {

        Page page = this.selectPage(new Query<JournalOrderEntity>(params).getPage(), new EntityWrapper<JournalOrderEntity>().eq("user_id", userId)
                .like("journal_title", params.get("journalTitle").toString()).eq("is_deleted", MagazineConstant.FALSE));
        return new PageUtils<JournalOrderEntity>(page);
    }

    @Override
    public PageUtils<JournalOrderVo> queryByOther(Map<String, Object> params) {
        Integer startLimit = (Integer.parseInt(String.valueOf(params.get("page"))) - 1) * Integer.parseInt(String.valueOf(params.get("limit")));
        params.put("startLimit", startLimit);
        List<JournalOrderVo> selectAll = orderMapper.pageTerm(params);

        Page page = new Page<>();
        page.setRecords(selectAll);
        page.setTotal(orderMapper.pageCount(params));


        page.setCurrent(Integer.parseInt(String.valueOf(params.get("page"))));

        return new PageUtils<JournalOrderVo>(page);
    }

    @Override
    public JournalOrderVo info(Long id) {
        return orderMapper.info(id);
    }

    @Override
    public JournalOrderEntity findByOutTradeNo(String outTradeNo) {
        return orderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public void saveWxOrder(Long journalId, HttpServletRequest request, HttpServletResponse response, Long userId) throws Exception {
        String ip = IpUtils.getIpAddr(request);

        JournalOrderEntity entity = new JournalOrderEntity();
        entity.setUserId(userId);
        entity.setJournalId(journalId);
        entity.setIp(IpUtils.getIpAddr(request));
//        entity.setIp("39.171.12.6");
        String codeUrl = save(entity);
        if (codeUrl == null) {
            throw new NullPointerException();
        }
        try {
            // 生成二维码
            Map<EncodeHintType, Object> hintx = new HashMap<>();
            // 设置纠错等级
            hintx.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            // 编码类型
            hintx.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hintx);
            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
//            response.addHeader("Access-Control-Allow-Origin", "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean deleteByIds(Set<Long> ids) {
        return orderMapper.deleteByIds(ids);
    }

    public String save(JournalOrderEntity journalOrderEntity) throws Exception {

        // 查找视频信息
        JournalEntity journalEntity = journalService.selectById(journalOrderEntity.getJournalId());

        // 生成订单
        journalOrderEntity.setJournalImg(journalEntity.getCoverImg());
        journalOrderEntity.setJournalTitle(journalEntity.getTitle());
        journalOrderEntity.setTotalFee(journalEntity.getPrice());
        journalOrderEntity.setCreateTime(new Date());
        journalOrderEntity.setState(0);
        journalOrderEntity.setIsDeleted(0);
        journalOrderEntity.setOutTradeNo(CommonUtils.generateUUID());
        orderMapper.insert(journalOrderEntity);

        // 获取codeUrl
        String codeUrl = unifiedOrder(journalOrderEntity);

        return codeUrl;
    }

    /**
     * 统一下单方法
     *
     * @param journalOrderEntity
     * @return
     */
    private String unifiedOrder(JournalOrderEntity journalOrderEntity) throws Exception {

        // 生成签名
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", journalOrderEntity.getJournalTitle());
        params.put("out_trade_no", journalOrderEntity.getOutTradeNo());
        params.put("total_fee", journalOrderEntity.getTotalFee().toString());
        params.put("spbill_create_ip", journalOrderEntity.getIp());
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        params.put("trade_type", "NATIVE");

        // sign签名
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);

//        Boolean flag =  WXPayUtil.isCorrectSign(params,weChatConfig.getKey());

        // map 转xml
        String payXml = WXPayUtil.mapToXml(params);
        System.out.println(payXml);

        // 统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(), payXml, 4000);
        if (null == orderStr) {
            return null;
        }
        Map<String, String> unifiedOrderMap = WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        // 获取codeUrl
        if (unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }
        return null;
    }


}
