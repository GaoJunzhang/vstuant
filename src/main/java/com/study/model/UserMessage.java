package com.study.model;

import javax.persistence.*;

@Table(name = "user_message")
public class UserMessage {
    private Integer uid;

    @Column(name = "media_id")
    private Integer mediaId;

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
     * @return media_id
     */
    public Integer getMediaId() {
        return mediaId;
    }

    /**
     * @param mediaId
     */
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }
}