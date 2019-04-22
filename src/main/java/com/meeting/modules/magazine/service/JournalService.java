package com.meeting.modules.magazine.service;

import com.baomidou.mybatisplus.service.IService;
import com.meeting.common.utils.PageUtils;
import com.meeting.modules.magazine.entity.JournalEntity;

import java.util.Map;
import java.util.Set;

public interface JournalService extends IService<JournalEntity> {

    /**
     * 管理员分页查询
     *
     * @param params
     * @return
     */
    PageUtils<JournalEntity> queryAdminTerm(Map<String, Object> params);

    /**
     * 分页杂志信息查询
     *
     * @param params
     * @return
     */
    PageUtils<JournalEntity> queryTerm(Map<String, Object> params);

    /**
     * 分页查询杂志上线信息
     *
     * @param params
     * @return
     */
    PageUtils<JournalEntity> pageOnline(Map<String, Object> params);

    /**
     * 保存杂志信息
     *
     * @param entity
     * @return
     */
    Boolean save(JournalEntity entity);

    /**
     * 更新杂志信息
     *
     * @param entity
     * @return
     */
    Boolean replace(JournalEntity entity);

    /**
     * 审核或取消审核
     *
     * @param id
     * @return
     */
    Boolean setOnline(Long id);

    /**
     * 单独删除
     *
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 批量删除
     *
     * @param ids
     */
    Boolean deleteByIds(Set<Long> ids);

}
