package com.study.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by user on 2018/6/8.
 */
@Data
public class UserMessageBean implements Serializable{
    private static final long serialVersionUID = 7920411889212657024L;
    private Integer uid;

    private Integer mediaId;

    private String mediaName;

    private String name;
}
