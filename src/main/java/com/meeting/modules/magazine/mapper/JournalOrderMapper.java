package com.meeting.modules.magazine.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import com.meeting.modules.magazine.vo.JournalOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-4-10
 */
@Mapper
public interface JournalOrderMapper extends BaseMapper<JournalOrderEntity> {

    /**
     * 分页查询
     *
     * @param map
     * @return
     */
    List<JournalOrderVo> pageTerm(Map<String, Object> map);

    /**
     * 分页统计数量
     *
     * @param map
     * @return
     */
    int pageCount(Map<String, Object> map);

    /**
     * 根据id查询订单信息
     *
     * @param id
     * @return
     */
    JournalOrderVo info(@Param("id") Long id);

    /**
     * 根据订单唯一标示查询订单信息
     *
     * @param outTradeNo
     * @return
     */
    JournalOrderEntity findByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    /**
     * 根据流水号关系数据
     *
     * @param journalOrderEntity
     * @return
     */
    int updateJournalOrderByOutTradeNo(JournalOrderEntity journalOrderEntity);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(@Param("ids") Set<Long> ids);

}
