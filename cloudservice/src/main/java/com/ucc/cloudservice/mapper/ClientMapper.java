package com.ucc.cloudservice.mapper;

import com.ucc.cloudservice.pojo.Client;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ClientMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Client record);

    Client selectByPrimaryKey(Integer id);

    List<Client> selectAll();

    int updateByPrimaryKey(Client record);

    Client selectByUsernameAndPassword(String username, String password);
}