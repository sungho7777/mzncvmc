package com.in.mzncvmc.content.neo.mapper;

import com.in.mzncvmc.content.neo.Neo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NeoMapper {
    List<Neo> findAll();
    Neo findById(Long id);
    void insert(Neo neo);
    void update(Neo neo);
    void delete(Long id);
}
