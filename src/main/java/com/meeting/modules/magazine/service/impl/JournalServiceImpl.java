package com.meeting.modules.magazine.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.Query;
import com.meeting.modules.magazine.constant.MagazineConstant;
import com.meeting.modules.magazine.entity.JournalEntity;
import com.meeting.modules.magazine.mapper.JournalMapper;
import com.meeting.modules.magazine.service.JournalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 杂志实现类
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-12-25
 */
@Service
public class JournalServiceImpl extends ServiceImpl<JournalMapper, JournalEntity> implements JournalService {


    private final JournalMapper journalMapper;

    @Autowired
    public JournalServiceImpl(JournalMapper journalMapper) {
        this.journalMapper = journalMapper;
    }

    @Override
    public PageUtils<JournalEntity> queryAdminTerm(Map<String, Object> params) {
        Page<JournalEntity> page = this.selectPage(new Query<JournalEntity>(params).getPage(),
                new EntityWrapper<>());
        return new PageUtils<JournalEntity>(page);
    }

    @Override
    public PageUtils<JournalEntity> queryTerm(Map<String, Object> params) {
        String title = (String) params.get("title");
        Page<JournalEntity> page = this.selectPage(new Query<JournalEntity>(params).getPage(),
//                new EntityWrapper<JournalEntity>().eq("online", MagazineConstant.TRUE));
                new EntityWrapper<JournalEntity>().like(StringUtils.isNotBlank(title), "title", title).eq("is_deleted", MagazineConstant.FALSE));
        return new PageUtils<>(page);
    }

    @Override
    public PageUtils<JournalEntity> pageOnline(Map<String, Object> params) {
        String title = (String) params.get("title");
        Page<JournalEntity> page = this.selectPage(new Query<JournalEntity>(params).getPage(),
                new EntityWrapper<JournalEntity>().like(StringUtils.isNotBlank(title), "title", title).eq("is_deleted", MagazineConstant.FALSE).eq("online", MagazineConstant.TRUE));
        return new PageUtils<>(page);
    }

    @Override
    public Boolean save(JournalEntity entity) {
        entity.setCreateTime(new Date());
        // 默认评分8.7
        if (entity.getPoint() == 0) {
            entity.setPoint(8.7);
        }
        return insert(entity);
    }

    @Override
    public Boolean replace(JournalEntity entity) {
        return this.updateById(entity);
    }

    @Override
    public Boolean setOnline(Long id) {
        JournalEntity entity = new JournalEntity();
        entity.setId(id);
        if (MagazineConstant.FALSE.equals(this.selectById(id).getOnline())) {
            entity.setOnline(MagazineConstant.TRUE);
        } else {
            entity.setOnline(MagazineConstant.FALSE);
        }
        return this.updateById(entity);
    }

    @Override
    public Boolean deleteById(Long id) {
        JournalEntity entity = new JournalEntity();
        entity.setId(id);
        entity.setIsDeleted(MagazineConstant.TRUE);
        return this.updateById(entity);
    }

    @Override
    public Boolean deleteByIds(Set<Long> ids) {
        JournalEntity entity = new JournalEntity();
        entity.setIsDeleted(MagazineConstant.TRUE);
        return this.update(entity, new EntityWrapper<JournalEntity>().in("id", ids));
    }
}
