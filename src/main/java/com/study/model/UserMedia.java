package com.study.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_media")
public class UserMedia implements Serializable {
    private static final long serialVersionUID = -2683166210266813865L;
    @Id
    private Integer id;

    private Integer uid;

    private Integer mid;

    private Date playtime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @return mid
     */
    public Integer getMid() {
        return mid;
    }

    /**
     * @param mid
     */
    public void setMid(Integer mid) {
        this.mid = mid;
    }

    /**
     * @return playtime
     */
    public Date getPlaytime() {
        return playtime;
    }

    /**
     * @param playtime
     */
    public void setPlaytime(Date playtime) {
        this.playtime = playtime;
    }
}