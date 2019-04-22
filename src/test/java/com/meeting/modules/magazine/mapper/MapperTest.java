package com.meeting.modules.magazine.mapper;

import com.meeting.MeetingApplication;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import com.meeting.modules.magazine.vo.JournalOrderVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-04-18
 */
@SpringBootTest(classes = MeetingApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MapperTest {

    @Autowired
    private JournalOrderMapper mapper;

    @Test
    public void  outTradeNo(){
        String outTradeNo = "0fd7c4167e9b41a2861bb9113bb68eaf";
        JournalOrderVo info = mapper.info(1L);
        JournalOrderEntity byOutTradeNo = mapper.findByOutTradeNo(outTradeNo);
        System.out.println(byOutTradeNo);
    }
}
