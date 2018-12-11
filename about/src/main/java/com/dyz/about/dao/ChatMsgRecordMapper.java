package com.dyz.about.dao;

import com.dyz.about.model.ChatMsgRecord;
import com.dyz.about.model.ChatMsgRecordExample;
import java.util.List;

import com.dyz.about.model.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ChatMsgRecordMapper {
    long countByExample(ChatMsgRecordExample example);

    int deleteByExample(ChatMsgRecordExample example);

    int insert(ChatMsgRecord record);

    int insertSelective(ChatMsgRecord record);

    List<ChatMsgRecord> selectByExample(ChatMsgRecordExample example);

    int updateByExampleSelective(@Param("record") ChatMsgRecord record, @Param("example") ChatMsgRecordExample example);

    int updateByExample(@Param("record") ChatMsgRecord record, @Param("example") ChatMsgRecordExample example);
    Classes findClassByID(Integer id);
}