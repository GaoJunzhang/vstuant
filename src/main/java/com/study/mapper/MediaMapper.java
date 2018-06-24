package com.study.mapper;

import com.study.model.Media;
import com.study.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediaMapper extends MyMapper<Media> {
    public Media findMediaByName(@Param("name")String name);

    public int insertMedia(Media media);

//    public Integer updateMedia(@Param("playcount") Integer playcount,@Param("id") Integer id);

//    int sumPalyCount(Integer uid);//用户总的播放次数
    int totalMedia();

    public List<Media> queryMediaByUid(@Param("uid") int uid);
}