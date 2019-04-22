package com.meeting.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import com.meeting.common.exception.RRException;
import com.meeting.common.utils.DateUtils;
import com.meeting.modules.sys.dao.SysCaptchaDao;
import com.meeting.modules.sys.entity.SysCaptchaEntity;
import com.meeting.modules.sys.service.SysCaptchaService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 验证码
 *
 * @author maoxinmin
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> implements SysCaptchaService {
	@Autowired
	private Producer producer;

	@Override
	public BufferedImage getCaptcha(String uuid) {
		if (StringUtils.isBlank(uuid)) {
			throw new RRException("uuid不能为空");
		}
		// 生成文字验证码
		String code = producer.createText();

		SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
		captchaEntity.setUuid(uuid);
		captchaEntity.setCode(code);
		// 5分钟后过期
		captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
		this.insert(captchaEntity);

		return producer.createImage(code);
	}

	@Override
	public boolean validate(String uuid, String code) {
		SysCaptchaEntity captchaEntity = this.selectOne(new EntityWrapper<SysCaptchaEntity>().eq("uuid", uuid));
		if (captchaEntity == null) {
			return false;
		}

		// 删除验证码
		this.deleteById(uuid);

		return captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System
				.currentTimeMillis();

	}
}
