package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.bean.MonthBean;
import com.study.bean.UserBean;
import com.study.bean.UsereMediaBean;
import com.study.mapper.UserMediaMapper;
import com.study.model.UserMedia;
import com.study.service.UserMediaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service("userMediaService")
public class UserMediaServiceImpl extends BaseService<UserMedia> implements UserMediaService {
    @Resource
    private UserMediaMapper userMediaMapper;

    public int sumPalyCount(Integer uid) {
        return userMediaMapper.sumPalyCount(uid);
    }

    public List<UserMedia> listByUid(Integer uid) {
        return userMediaMapper.listByUid(uid);
    }

    public PageInfo<UsereMediaBean> queryUserMediaByUname(String username, String startTime, String endTime, int start, int length, Integer uid) {

        int page = start / length + 1;
        PageHelper.startPage(page, length);
        List<UsereMediaBean> usereMediaBeanList = userMediaMapper.queryUserMediaByUname(username, startTime, endTime, uid);
        return new PageInfo<>(usereMediaBeanList);
    }

    public int sumPlayCount(Integer uid) {
        return userMediaMapper.sumPalyCount(uid);
    }

    public List<MonthBean> statisticsByYear(Integer year, Integer uid) {
        return userMediaMapper.statisticsByYear(year, uid);
    }

    public PageInfo<UsereMediaBean> mediaPlayByUid(Integer uid, String startTime, String endTime, int start, int length) {
        int page = start / length + 1;
        PageHelper.startPage(page, length);
        List<UsereMediaBean> usereMediaBeanList = userMediaMapper.mediaPlayByUid(uid, startTime, endTime);
        return new PageInfo<>(usereMediaBeanList);
    }

    public PageInfo<UserBean> userMediaStatistics(String username,String realyname, String startTime, String endTime, int start, int length) {
        int page = start / length + 1;
        PageHelper.startPage(page, length);
        List<UserBean> userBeanList = userMediaMapper.userMediaStatistics(username,realyname, startTime, endTime);
        return new PageInfo<>(userBeanList);
    }

    public int thisMonthPlayCount() {
        return userMediaMapper.thisMonthPlayCount();
    }

    public int totalPlayCount() {
        return userMediaMapper.totalPlayCount();
    }

    public int totalSunPlayCount(Integer uid){
        return userMediaMapper.totalSunPlayCount(uid);
    }

    public int getUserMediaCount(String mediaName, String startTime, String endTime){
        return userMediaMapper.getUserMediaCount(mediaName,startTime,endTime);
    }


}
