package com.study.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by user on 2018/6/8.
 */
@RestController
@RequestMapping(value = "/userMessage")
public class UserMessageController {
 /*   @Resource
    private UserMessageService userMessageService;

    @RequestMapping(value = "/userMessages",method = RequestMethod.GET)
    public List<UserMessageBean> userMessages(@RequestParam(name = "uid",required = true) int uid){
        List<UserMessageBean> userMessageBeans = userMessageService.queryByUid(uid);
        return userMessageBeans;
    }*/
}
