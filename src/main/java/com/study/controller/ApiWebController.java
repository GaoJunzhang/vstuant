package com.study.controller;

import com.study.model.Media;
import com.study.model.User;
import com.study.model.UserMedia;
import com.study.result.JsonResult;
import com.study.result.ResultCode;
import com.study.service.MediaService;
import com.study.service.UserMediaService;
import com.study.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/web")
public class ApiWebController {
    @Resource
    private MediaService mediaService;

    @Resource
    private UserMediaService userMediaService;

    @Resource
    private UserService userService;

    //插入媒体数据，如果媒体存在则+1播放次数
    @RequestMapping(value = "/postUserMedia")
    public Object postUserMedia(@Param("mediaName") String mediaName, HttpServletRequest request) {
        JsonResult jsonResult;
        String username = request.getAttribute("username") + "";
        Map<String, Object> map = map = new HashMap<String, Object>();
        try {
            User user = userService.selectByUsername(username);
            int spalycount = userMediaService.sumPalyCount(user.getId());
            if (user.getIsLimit() == 0) {//是否限制次数（0是1否）
                map.put("playLimit", user.getIsLimit());
                map.put("userTotalPlay", user.getSumcount());
                if (user.getSumcount() <= 0) {
                    map.put("validPlay", 0);
                    map.put("usedPlay", 0);
                    jsonResult = new JsonResult(ResultCode.UNAUTHORIZED, "该账号未授权播放", map);
                    return jsonResult;
                }
                if (user.getSumcount() <= spalycount) {
                    map.put("validPlay", 0);
                    map.put("usedPlay", spalycount);
                    jsonResult = new JsonResult(ResultCode.UNAUTHORIZED, "该账号可播放次数为0", map);
                    return jsonResult;
                }
            } else {
                map.put("validPlay", 0);
                map.put("userTotalPlay", 0);
                map.put("playLimit", user.getIsLimit());
            }
            Media media = mediaService.findByName(mediaName.trim());
            if (media == null) {
                media = new Media();
                media.setName(mediaName.trim());
                media.setCreatetime(new Timestamp(System.currentTimeMillis()));
                media.setUid(user.getId());
                mediaService.insertMedia(media);
            }
            UserMedia userMedia = new UserMedia();
            userMedia.setUid(user.getId());
            userMedia.setMid(media.getId());
            userMedia.setPlaytime(new Timestamp(System.currentTimeMillis()));
            userMediaService.save(userMedia);
            spalycount += 1;
            map.put("usedPlay", spalycount);
            if (user.getIsLimit()==0){

                map.put("validPlay", user.getSumcount() - spalycount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = new JsonResult(ResultCode.EXCEPTION, "系统异常", e);
            return jsonResult;
        }
        jsonResult = new JsonResult(ResultCode.SUCCESS, "成功", map);
        return jsonResult;
    }

    @RequestMapping(value = "/getMediaPlayInfo")
    public Object getMediaPlayInfo(HttpServletRequest request) {
        JsonResult jsonResult;
        String username = request.getAttribute("username") + "";

        User user = userService.selectByUsername(username);
        Map<String, Object> map = map = new HashMap<String, Object>();
        int spalycount = userMediaService.sumPalyCount(user.getId());
        if (user.getIsLimit() == 0) {//是否限制次数（0是1否）
            map.put("userTotalPlay", user.getSumcount());
            map.put("validPlay", user.getSumcount() - spalycount);
           /* if (user.getSumcount() <= 0) {
                jsonResult = new JsonResult(ResultCode.UNAUTHORIZED, "该账号未授权播放", map);
                return jsonResult;
            }*/
        } else {
            map.put("userTotalPlay", 0);
            map.put("validPlay", 0);
        }
        map.put("playLimit", user.getIsLimit());
        map.put("usedPlay", spalycount);
        jsonResult = new JsonResult(ResultCode.SUCCESS, "成功", map);
        return jsonResult;
    }

    @RequestMapping(value = "/logout")
    public Object logout(HttpServletRequest request) {
        JsonResult jsonResult;
        String username = request.getAttribute("username") + "";

        userService.updateLoggerByUname(null, new Timestamp(System.currentTimeMillis()), username);
        jsonResult = new JsonResult(ResultCode.SUCCESS, "注销成功", null);
        return jsonResult;
    }

}
