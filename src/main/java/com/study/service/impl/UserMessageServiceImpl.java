package com.study.service.impl;

import com.study.bean.UserMessageBean;
import com.study.mapper.UserMessageMapper;
import com.study.model.UserMessage;
import com.study.service.UserMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by user on 2018/6/8.
 */
@Service("userMessageService")
public class UserMessageServiceImpl extends BaseService<UserMessage> implements UserMessageService {
    @Resource
    private UserMessageMapper userMessageMapper;

    public void batchInsert(List<UserMessage> list){
        userMessageMapper.batchInsert(list);
    }

    public int deleteByUid(Integer uid){
        return userMessageMapper.deleteByUid(uid);
    }
}
