package com.nasi.todolist.mapper;

import com.nasi.todolist.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userMapper {
    @Insert("insert into `User`(UUID,ID,PASSWD,EMAIL) values(#{UUID},#{ID},#{PASSWD},#{EMAIL})")
    int addUser(User user);
}
