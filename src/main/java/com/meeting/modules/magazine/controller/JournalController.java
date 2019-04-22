package com.meeting.modules.magazine.controller;

import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.R;
import com.meeting.modules.magazine.entity.JournalEntity;
import com.meeting.modules.magazine.service.JournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 杂志控制层
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-01-09
 */
@RestController
@RequestMapping("/api/v1/journal")
public class JournalController {

    private final JournalService service;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public JournalController(JournalService service) {
        this.service = service;
    }

    /**
     * 根据id查询杂志信息
     *
     * @param id
     * @return
     */
    @GetMapping("/query_id/{id}")
    public R queryById(@PathVariable Integer id) {
        return R.ok().put("data", service.selectById(id));
    }

    /**
     * 获取全部杂志信息的分页
     *
     * @param params
     * @return
     */
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = service.queryTerm(params);
        return R.ok().put("list", page);
    }

    /**
     * 获取全部杂志信息的分页
     *w19940323
     * @param params
     * @return
     */
    @GetMapping("/page_online")
    public R pageInOnline(@RequestParam Map<String, Object> params) {
        PageUtils page = service.pageOnline(params);
        return R.ok().put("list", page);
    }

    /**
     * 保存杂志信息
     *
     * @param journalEntity
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody JournalEntity journalEntity) {
        service.save(journalEntity);
        return R.ok();
    }

    /**
     * 更新杂志信息
     *
     * @param journalEntity
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody JournalEntity journalEntity) {
        service.replace(journalEntity);
        return R.ok();
    }


    /**
     * 审核或取消审核
     *
     * @param id
     * @return
     */
    @GetMapping("set_online")
    public R setOnline(@RequestParam Long id) {
        service.setOnline(id);
        return R.ok();
    }

    /**
     * 单独删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Set<Long> ids) {
        service.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/ids")
    public R deleteByIds(@RequestBody Set<Long> ids) {
        service.deleteByIds(ids);
        return R.ok();
    }


}
