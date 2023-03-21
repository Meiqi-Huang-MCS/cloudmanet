package com.ucc.cloudservice.mapper;

import com.ucc.cloudservice.pojo.Manet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ManetMapper {
    int deleteByPrimaryKey(Integer netid);

    int insert(Manet record);

    Manet selectByPrimaryKey(Integer netid);

    List<Manet> selectAll();

    int updateByPrimaryKey(Manet record);
}