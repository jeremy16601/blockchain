package com.blockchain.robot.util;

import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.service.api.DingHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:auth.properties")
public class LoggerUtil {

    @Autowired
    private DingHttpClient dingLogger;//通知和日志

    @Value("${dingding.token}")
    private String ding_token;


    public void info(Class cls, String message) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.info(message);
    }

    public void infoWithNotify(Class cls, String message) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.info(message);

        dingLogger.ding(toString(), DingMessage.newInstance(message));
    }

}
