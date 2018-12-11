package com.dyz.about.dao;

import com.dyz.about.model.Classes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassMapper {
    Classes getClass2(Integer id);
    Classes getClass3(Integer id);
}
