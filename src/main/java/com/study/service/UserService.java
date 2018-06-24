package com.study.service;

import com.github.pagehelper.PageInfo;
import com.study.model.User;
import com.study.model.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by yangqj on 2017/4/21.
 */
public interface UserService extends IService<User> {
    PageInfo<User> selectByPage(User user, int start, int length);

    User selectByUsername(String username);

    void delUser(Integer userid);

    void updateEquipmentNoByUsername(User user);

    public String batchImport(String fileName, MultipartFile mfile);

    public void batchUpdateCount(List<User> users);

    int totalUser();

    List<User> findAll();

    public void updateLoggerByUname(Timestamp loginTime, Timestamp logoutTime, String username);

    public void updateLimit(short isLimit,int id);

    public void updateRemark(int id,String remark);

}
