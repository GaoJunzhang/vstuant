package com.study.bean;

import lombok.Data;

/**
 * class_name: UserBean
 * package: com.study.bean
 * describe: 用户bean
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2018/5/22
 * creat_time: 13:31
 **/
@Data
public class UserBean {
    private Integer id;

    private String username;

    private String password;

    /**
     * 是否启用
     */
    private Integer enable;

    private Integer sumcount;

    private String position;

    private String createTime;

    private String realyname;

    private Integer usedPlayCount;

    private Integer vaildPlayCount;

    private Integer playprogress;

    private Short isLimit;

    private String remark;

    public Integer getVaildPlayCount() {
        return this.sumcount - this.usedPlayCount;
    }
}
