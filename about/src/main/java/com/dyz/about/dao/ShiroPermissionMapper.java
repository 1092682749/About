package com.dyz.about.dao;

import com.dyz.about.model.Permission;
import com.dyz.about.model.ShiroPermission;
import com.dyz.about.model.ShiroPermissionExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ShiroPermissionMapper {
    long countByExample(ShiroPermissionExample example);

    int deleteByExample(ShiroPermissionExample example);

    int insert(ShiroPermission record);

    int insertSelective(ShiroPermission record);

    List<ShiroPermission> selectByExample(ShiroPermissionExample example);

    int updateByExampleSelective(@Param("record") ShiroPermission record, @Param("example") ShiroPermissionExample example);

    int updateByExample(@Param("record") ShiroPermission record, @Param("example") ShiroPermissionExample example);

    ShiroPermission findByID(Integer id);
    List<ShiroPermission> findByUID(Integer uid);
}