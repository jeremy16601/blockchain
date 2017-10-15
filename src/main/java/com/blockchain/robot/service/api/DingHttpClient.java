package com.blockchain.robot.service;

import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.util.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dingding", url = "https://oapi.dingtalk.com", configuration = FeignConfiguration.class)
public interface DingHttpClient {

    @RequestMapping(value = "/robot/send", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    String ding(@RequestParam("access_token") String access_token, DingMessage message);

}