package com.study.controller;

import com.study.model.Media;
import com.study.model.Resources;
import com.study.model.User;
import com.study.service.MediaService;
import com.study.service.ResourcesService;
import com.study.service.UserMediaService;
import com.study.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/21.
 */
@Controller
public class HomeController {

    @Resource
    private UserMediaService userMediaService;

    @Resource
    private UserService userService;

    @Resource
    private MediaService mediaService;

    @Resource
    private ResourcesService resourcesService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, User user, Model model) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            request.setAttribute("msg", "用户名或密码不能为空！");
            return "login";
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            return "redirect:statisticsPage";
        } catch (LockedAccountException lae) {
            token.clear();
            request.setAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
            return "login";
        } catch (AuthenticationException e) {
            token.clear();
            request.setAttribute("msg", "用户或密码不正确！");
            return "login";
        }
    }

    @RequestMapping(value = {"/usersPage"})
    public String usersPage() {
        return "user/users";
    }

    @RequestMapping("/rolesPage")
    public String rolesPage() {
        return "role/roles";
    }

    @RequestMapping("/resourcesPage")
    public String resourcesPage() {
        return "resources/resources";
    }

    @RequestMapping("/500")
    public String fornot() {
        return "500";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    @RequestMapping("/mediaPage")
    public String viewMedias() {
        return "media/medias";
    }

    @RequestMapping("/myMediaPage")
    public String viewuserMedias(Model model) {
        Integer uid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        model.addAttribute("totalPlayCount",userMediaService.totalSunPlayCount(uid));
        return "myMedia/mymedias";
    }

    @RequestMapping("/viewMymedia")
    public String viewMymedia() {
        return "userMedia/userMedias";
    }

    @RequestMapping("/userMediaPage")
    public String viewMediaStatistics() {
        return "userMedia/userMedias";
    }

    @RequestMapping(value = {"/statisticsPage", ""})
    public String statisticsPage(Model model) {
        User u = (User) SecurityUtils.getSubject().getSession().getAttribute("userSession");
        User user = userService.selectByUsername(u.getUsername());

//        Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        boolean flag = true;
        Map<String,Object> map = new HashMap<>();
        map.put("type",1);
        map.put("userid",user.getId());
        List<Resources> resourcesList = resourcesService.loadUserResources(map);
        for(int i=0 ;i<resourcesList.size();i++){
            if (resourcesList.get(i).getResurl().contains("statisticsPage")){
                flag = false;
                break;
            }
        }
        model.addAttribute("remark",user.getRemark());
        model.addAttribute("sumMonthPlay",userMediaService.thisMonthPlayCount());
        model.addAttribute("totalPlay",userMediaService.totalPlayCount());
        model.addAttribute("totalUsers",userService.totalUser());
        model.addAttribute("totalMedias",mediaService.totalMedia());
        List<Media> userMessageBeans = mediaService.queryMediaByUid(user.getId());
        String userMessages = "";
        if (userMessageBeans.size()>0){
            for (int i=0; i<userMessageBeans.size(); i++){
                userMessages += userMessageBeans.get(i).getName()+",";
            }
            userMessages = userMessages.substring(0,userMessages.length()-1);
        }else {
            userMessages = "欢迎登录！"+user.getRealyname();
        }
        model.addAttribute("userMessages",userMessages);
        if (flag){
            model.addAttribute("uName",user.getUsername());
            model.addAttribute("uid",user.getId());
            model.addAttribute("totalPlayCount",userMediaService.totalSunPlayCount(user.getId()));
            return "myMedia/mymedias";
        }else {
            return "mediaStatitics/mediaStatistics";
        }
    }

    @RequestMapping("/userMediaInfo")
    public String userMediaInfo(User user, Model model) {
        User user1 = userService.selectByUsername(user.getUsername());
        model.addAttribute("totalPlayCount",userMediaService.totalSunPlayCount(user1.getId()));
        model.addAttribute("uid", user1.getId());
        model.addAttribute("uName", user1.getUsername()+"");
        model.addAttribute("realyName", user1.getRealyname()+"");
        List<Media> userMessageBeans = mediaService.queryMediaByUid(user1.getId());
        String userMessages = "";
        if (userMessageBeans.size()>0){
            for (int i=0; i<userMessageBeans.size(); i++){
                userMessages += userMessageBeans.get(i).getName()+",";
            }
            userMessages = userMessages.substring(0,userMessages.length()-1);
        }else {
            userMessages = "欢迎登录！"+user1.getRealyname();
        }
        model.addAttribute("remark",user1.getRemark());
        model.addAttribute("userMessages",userMessages);
        return "userMediaInfo/userMediaInfo";
    }


}
