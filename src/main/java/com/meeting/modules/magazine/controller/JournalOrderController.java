package com.meeting.modules.magazine.controller;

import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.R;
import com.meeting.modules.magazine.service.JournalOrderService;
import com.meeting.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * 杂志订阅表
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-04-10
 */
@RestController
@RequestMapping("/api/v1/journal_order")
public class JournalOrderController extends AbstractController {

    private final JournalOrderService service;

    @Autowired
    public JournalOrderController(JournalOrderService service) {
        this.service = service;
    }

    /**
     * 订单分页情况表
     *
     * @param params
     * @return
     */
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = service.queryByOther(params);
        return R.ok().put("list", page);
    }

    /**
     * 根据id查询杂志信息
     *
     * @param id
     * @return
     */
    @GetMapping("/query_id/{id}")
    public R queryById(@PathVariable Long id) {
        return R.ok().put("data", service.info(id));
    }


    /**
     * 新增订单
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/add")
    public R saveOrder(@RequestParam(value = "journalId", required = true) Long id, HttpServletRequest request, HttpServletResponse response) {
        service.saveOrder(id, request);
        return R.ok();
    }

    /**
     * 微信支付接口
     *
     * @param id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/wxadd")
    public void saveWxOrder(@RequestParam(value = "journalId", required = true) Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        service.saveWxOrder(id, request, response, getUserId());
//        return R.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Set<Long> ids) {
        service.deleteByIds(ids);
        return R.ok();
    }
}
