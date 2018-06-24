package com.study.mapper;

import com.study.bean.MonthBean;
import com.study.bean.UserBean;
import com.study.bean.UsereMediaBean;
import com.study.model.UserMedia;
import com.study.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMediaMapper extends MyMapper<UserMedia> {
    int sumPalyCount(@Param("uid") Integer uid);

    public List<UserMedia> listByUid(@Param("uid") Integer uid);

    public List<UsereMediaBean> queryUserMediaByUname(@Param("username") String username, @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime, @Param("uid") Integer uid);

    public int sumPlayCount(@Param("uid") Integer uid);

    public List<MonthBean> statisticsByYear(@Param("year") Integer year, @Param("uid") Integer uid);

    public List<UsereMediaBean> mediaPlayByUid(@Param("uid") Integer uid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    public List<UserBean> userMediaStatistics(@Param("username") String username,@Param("realyname") String realyname, @Param("startTime") String startTime,
                                              @Param("endTime") String endTime);

    public int thisMonthPlayCount();

    public int totalPlayCount();

    public int totalSunPlayCount(@Param("uid") Integer uid);

    public int getUserMediaCount(@Param("mediaName") String mediaName, @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);


}