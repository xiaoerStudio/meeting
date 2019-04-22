package com.meeting.modules.magazine.service;

import com.baomidou.mybatisplus.service.IService;
import com.meeting.common.utils.PageUtils;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import com.meeting.modules.magazine.vo.JournalOrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public interface JournalOrderService extends IService<JournalOrderEntity> {

    /**
     * 订阅
     *
     * @param journalId
     * @param request
     * @return
     */
    Boolean saveOrder(Long journalId, HttpServletRequest request);

    /**
     * 我的订阅的信息
     *
     * @param userId
     * @return
     */
    PageUtils<JournalOrderEntity> queryByUserId(Map<String, Object> params, Long userId);

    /**
     * 根据条件查询订单信息
     *
     * @param params
     * @return
     */
    PageUtils<JournalOrderVo> queryByOther(Map<String, Object> params);

    /**
     * 根据id查询订单信息
     *
     * @param id
     * @return
     */
    JournalOrderVo info(Long id);

    /**
     * 根据订单唯一标识查询订单
     *
     * @param outTradeNo
     * @return
     */
    JournalOrderEntity findByOutTradeNo(String outTradeNo);

    /**
     * 微信支付订单
     *
     * @param journalId
     * @param request
     * @param response
     * @return
     */
    void saveWxOrder(Long journalId, HttpServletRequest request, HttpServletResponse response, Long userId) throws Exception;


    /**
     * 批量删除
     *
     * @param ids
     */
    Boolean deleteByIds(Set<Long> ids);
}
