package com.study.controller;

import com.github.pagehelper.PageInfo;
import com.study.model.User;
import com.study.model.UserMessage;
import com.study.model.UserRole;
import com.study.service.UserMessageService;
import com.study.service.UserRoleService;
import com.study.service.UserService;
import com.study.util.ExcelUtil;
import com.study.util.PasswordHelper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/22.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserMessageService userMessageService;

    @RequestMapping
    public Map<String, Object> getAll(User user, String draw,
                                      @RequestParam(required = false, defaultValue = "1") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        PageInfo<User> pageInfo = userService.selectByPage(user, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    /**
     * 保存用户角色
     *
     * @param userRole 用户角色
     *                 此处获取的参数的角色id是以 “,” 分隔的字符串
     * @return
     */
    @RequestMapping("/saveUserRoles")
    public String saveUserRoles(UserRole userRole) {
        if (StringUtils.isEmpty(userRole.getUserid()))
            return "error";
        try {
            userRoleService.addUserRole(userRole);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/add")
    public String add(User user) {
        User u = userService.selectByUsername(user.getUsername());
        if (u != null)
            return "error";
        try {
            user.setEnable(1);
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
//            user.setSumcount(user。);
            user.setIsLimit((short)0);
            userService.save(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(Integer id) {
        try {
            userService.delUser(id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/resetPassword")
    public String resetPassword(Integer id) {
        User user = userService.selectByKey(id);
        if (user == null)
            return "error";
        try {
            user.setEnable(1);
            user.setPassword("123456");
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            userService.updateEquipmentNoByUsername(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/updateCount")
    public String updateCount(Integer sumcount, Integer ids[]) {

        List<User> users = new ArrayList<User>(ids.length);
        for (int i = 0; i < ids.length; i++) {
            User user = new User();
            user.setId(ids[i]);
            user.setSumcount(sumcount);
            users.add(user);
        }
        try {
            userService.batchUpdateCount(users);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping("/downloadUsers")
    public void downloadUsers(HttpServletResponse response) throws IOException {
        String[] headers = {"账号", "真实姓名", "权限次数", "是否启用"};
        String fileName = "用户明细" + System.currentTimeMillis() + ".xlsx"; //文件名
        String sheetName = "用户明细";//sheet名
        List<User> list = userService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[][] values = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            values[i] = new String[headers.length];
            //将对象内容转换成string
            User user = list.get(i);
            values[i][0] = user.getUsername() + "";
            values[i][1] = user.getRealyname() + "";
            values[i][2] = user.getSumcount() + "";
            if (0 == user.getEnable()) {
                values[i][3] = "关闭";
            } else {
                values[i][3] = "开启";
            }
        }
        SXSSFWorkbook wb = ExcelUtil.getSXSSFWorkbook(sheetName, headers, values, null);
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @RequestMapping(value = "/editPwd",method = RequestMethod.POST)
    public String editPwd(HttpSession session, @RequestParam(value = "currentPwd")String currentPwd, @RequestParam(value = "newPwd")String newPwd){
        try {
            PasswordHelper passwordHelper = new PasswordHelper();
            User user = userService.selectByKey(session.getAttribute("userSessionId"));
            User user1 = new User();
            user1.setPassword(currentPwd);
            user1.setUsername(user.getUsername());
            String Pwd =passwordHelper.getPassword(user1);
            if (!Pwd.equals(user.getPassword())){
                return "pwdError";
            }
            user.setPassword(newPwd);
            passwordHelper.encryptPassword(user);
            userService.updateEquipmentNoByUsername(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/setLimit",method = RequestMethod.POST)
    public String updateLimit(@RequestParam(value = "id") int id,@RequestParam(value = "isLimit") short isLimit){
        try {
            userService.updateLimit(isLimit,id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "updateRemark",method = RequestMethod.POST)
    public String updateRemark(@RequestParam(value = "id") int id,@RequestParam(value = "remark") String remark,Integer ids[]){
        try {
            if (ids.length>0){
                System.out.println(userMessageService.deleteByUid(id));
                List<UserMessage> list = new ArrayList<UserMessage>(ids.length);
                for (int i=0;i<ids.length;i++){
                    UserMessage userMessage = new UserMessage();
                    userMessage.setUid(id);
                    userMessage.setMediaId(ids[i]);
                    list.add(userMessage);
                }
                userMessageService.batchInsert(list);
            }
            userService.updateRemark(id,remark);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
