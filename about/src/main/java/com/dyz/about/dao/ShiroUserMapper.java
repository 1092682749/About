package com.dyz.about.dao;

import com.dyz.about.model.ShiroUser;
import com.dyz.about.model.ShiroUserExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface ShiroUserMapper {
    long countByExample(ShiroUserExample example);

    int deleteByExample(ShiroUserExample example);

    int insert(ShiroUser record);

    int insertSelective(ShiroUser record);

    List<ShiroUser> selectByExample(ShiroUserExample example);

    int updateByExampleSelective(@Param("record") ShiroUser record, @Param("example") ShiroUserExample example);

    int updateByExample(@Param("record") ShiroUser record, @Param("example") ShiroUserExample example);
    ShiroUser findByID(Integer id);
    ShiroUser findByName(String name);
}