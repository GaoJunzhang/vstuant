package com.study.mapper;

import com.study.bean.UserMessageBean;
import com.study.model.UserMessage;
import com.study.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMessageMapper extends MyMapper<UserMessage> {
//    public List<UserMessageBean> queryByUid(@Param("uid") Integer uid);

    public void batchInsert(@Param("list") List<UserMessage> list);

    public int deleteByUid(@Param("uid") Integer uid);
}