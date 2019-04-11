package com.melly.springboot.mapper;

import com.melly.springboot.model.User;

import java.util.List;

public interface UserMapper {
    public List<User> findAll();
}
