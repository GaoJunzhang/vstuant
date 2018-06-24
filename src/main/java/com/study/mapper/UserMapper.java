package com.study.mapper;

import com.study.model.User;
import com.study.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserMapper extends MyMapper<User> {
    public void batchUpdateCount(@Param("list") List<User> list);

    int totalUser();

    public void updateLoggerByUname(@Param("loginTime") Timestamp loginTime, @Param("logoutTime") Timestamp logoutTime, @Param("username") String username);

    public void updateLimit(@Param("isLimit") short isLimit,@Param("id") Integer id);

    public void updateRemark(@Param("id") int id,@Param("remark") String remark);

}