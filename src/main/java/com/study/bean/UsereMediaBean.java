package com.study.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UsereMediaBean implements Serializable {
    private static final long serialVersionUID = 7962016652832954259L;

    private Integer id;

    private Integer uid;

    private Integer mid;

    private Date playtime;

    private String username;

    private String mediaName;

    private String usedPlayCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPlaytime() {
        return playtime;
    }
}
