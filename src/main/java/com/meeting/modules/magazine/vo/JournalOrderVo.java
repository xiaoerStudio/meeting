package com.meeting.modules.magazine.vo;

import com.meeting.modules.magazine.entity.JournalEntity;
import com.meeting.modules.magazine.entity.JournalOrderEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsExclude;

import javax.security.auth.callback.Callback;

/**
 * 订单vo
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JournalOrderVo extends JournalOrderEntity {

    /**
     * 用户名
     */
    private String userName;

}
