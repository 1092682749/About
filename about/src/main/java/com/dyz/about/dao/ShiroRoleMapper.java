package com.dyz.about.dao;

import com.dyz.about.model.Role;
import com.dyz.about.model.ShiroRole;
import com.dyz.about.model.ShiroRoleExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ShiroRoleMapper {
    long countByExample(ShiroRoleExample example);

    int deleteByExample(ShiroRoleExample example);

    int insert(ShiroRole record);

    int insertSelective(ShiroRole record);

    List<ShiroRole> selectByExample(ShiroRoleExample example);

    int updateByExampleSelective(@Param("record") ShiroRole record, @Param("example") ShiroRoleExample example);

    int updateByExample(@Param("record") ShiroRole record, @Param("example") ShiroRoleExample example);

    List<ShiroRole> findByID(Integer id);

    List<ShiroRole> findByUID(Integer uid);
}