package com.telek.hemsipc.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.HemsipcApplicationTests;

/**
 * @Auther: wll
 * @Date: 2018/9/10 15:25
 * @Description:
 */
@Component
public class LogbackTest extends HemsipcApplicationTests{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackTest.class);

    @Test
    public void logTest() {
        LOGGER.trace("logback的--trace日志--输出了");
        LOGGER.debug("logback的--debug日志--输出了");
        LOGGER.info("logback的--info日志--输出了");
        LOGGER.warn("logback的--warn日志--输出了");
        LOGGER.error("logback的--error日志--输出了");
    }
}
