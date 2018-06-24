package com.study.service;

import com.github.pagehelper.PageInfo;
import com.study.bean.MonthBean;
import com.study.bean.UserBean;
import com.study.bean.UsereMediaBean;
import com.study.model.UserMedia;

import java.util.List;

public interface UserMediaService extends IService<UserMedia> {
    int sumPalyCount(Integer uid);

    public List<UserMedia> listByUid(Integer uid);

    public PageInfo<UsereMediaBean> queryUserMediaByUname(String username, String startTime, String endTime, int start, int length, Integer uid);

    public int sumPlayCount(Integer uid);

    public List<MonthBean> statisticsByYear(Integer year, Integer uid);

    public PageInfo<UsereMediaBean> mediaPlayByUid(Integer uid, String startTime, String endTime, int start, int length);

    public PageInfo<UserBean> userMediaStatistics(String username, String realyname, String startTime, String endTime, int start, int length);

    public int thisMonthPlayCount();

    public int totalPlayCount();

    public int totalSunPlayCount(Integer uid);

    public int getUserMediaCount(String mediaName, String startTime, String endTime);
}
