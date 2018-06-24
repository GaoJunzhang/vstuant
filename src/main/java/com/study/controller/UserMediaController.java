package com.study.controller;

import com.github.pagehelper.PageInfo;
import com.study.bean.MonthBean;
import com.study.bean.UserBean;
import com.study.bean.UsereMediaBean;
import com.study.model.User;
import com.study.service.UserMediaService;
import com.study.service.UserService;
import com.study.util.VTools;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/userMedias")
public class UserMediaController {
    @Resource
    private UserMediaService userMediaService;

    @Resource
    private UserService userService;

    @RequestMapping
    public Map<String, Object> getAll(UsereMediaBean usereMediaBean, String draw,
                                      @RequestParam(required = false) String startTime,
                                      @RequestParam(required = false) String endTime,
                                      @RequestParam(required = false, defaultValue = "1") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        }
        PageInfo<UsereMediaBean> pageInfo = userMediaService.queryUserMediaByUname(usereMediaBean.getUsername(), startTime, endTime, start, length,null);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/myUserMediaData", method = RequestMethod.GET)
    public Map myUserMediaData(@RequestParam(required = false) String year ,@RequestParam(required = false) Integer uid,@RequestParam(required = false) String username) {
        User u = (User) SecurityUtils.getSubject().getSession().getAttribute("userSession");
        if (StringUtils.isEmpty(username)){
            username = u.getUsername();
        }
        User user = userService.selectByUsername(username);
        Map map = new HashMap();
//        if (StringUtils.isEmpty(year)) {
            year = VTools.getSysYear();
//        }
        List<MonthBean> list = userMediaService.statisticsByYear(Integer.parseInt(year), uid);
        if (user.getIsLimit()==1){
            map.put("sumCount", 100);
            map.put("sumPlayCount", 0);
            map.put("statisList", list);
        }else {

            if (StringUtils.isEmpty(uid)){

                uid = user.getId();
            }
            int sumPlayCount = userMediaService.sumPlayCount(uid);
            map.put("sumCount", user.getSumcount());
            map.put("sumPlayCount", sumPlayCount);
            map.put("statisList", list);
        }
        map.put("isLimit",user.getIsLimit());
        return map;
    }

    @RequestMapping(value = "/yearMediaData", method = RequestMethod.GET)
    public Map yearMediaData(@RequestParam(required = false) String year) {
//        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userSession");
        if (StringUtils.isEmpty(year)) {
            year = VTools.getSysYear();
        }
        List<MonthBean> list = userMediaService.statisticsByYear(Integer.parseInt(year), null);
        Map map = new HashMap();
        map.put("statisList", list);
        return map;
    }

    @RequestMapping(value = "/myUserMedias", method = RequestMethod.GET)
    public Map myUserMedias(String draw,
                            @RequestParam(required = false) Integer uid,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime,
                            @RequestParam(required = false, defaultValue = "1") int start,
                            @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        }
        User user = new User();
        if (StringUtils.isEmpty(uid)){
            user = (User) SecurityUtils.getSubject().getSession().getAttribute("userSession");
        }
        PageInfo<UsereMediaBean> pageInfo = userMediaService.queryUserMediaByUname(user.getUsername(), startTime, endTime, start, length,uid);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/myMediasPlayInfo", method = RequestMethod.GET)
    public Map myMediasPlayInfo(String draw,
                                @RequestParam(required = false) Integer uid,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime,
                                @RequestParam(required = false, defaultValue = "1") int start,
                                @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        }
        if (StringUtils.isEmpty(uid)){

            uid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        }
        PageInfo<UsereMediaBean> pageInfo = userMediaService.mediaPlayByUid(uid, startTime, endTime, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/userMediaStatistics", method = RequestMethod.GET)
    public Map userMediaStatistics(String draw, @RequestParam(required = false) String username, @RequestParam(required = false) String realyname,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endTime,
                                   @RequestParam(required = false, defaultValue = "1") int start,
                                   @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        }
//        Integer uid = (Integer)SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        PageInfo<UserBean> pageInfo = userMediaService.userMediaStatistics(username,realyname, startTime, endTime, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/getUserMediaCount",method = RequestMethod.GET)
    public String getUserMediaCount(@RequestParam(required = false) String mediaName,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime){
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        }
        return userMediaService.getUserMediaCount(mediaName,startTime,endTime)+"";
    }
}
