package com.ai.code.service;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

/**
 * 时间服务类
 * 包含获取当前时间的业务方法
 * 注意：方法访问权限需要为public，以便反射调用
 */
public class CurrentTimeService {
    /**
     * 获取当前日期时间
     * @return 当前日期时间的字符串表示
     */
    public String getCurrentDateTime() {
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
