package com.melly.springboot.service.impl;

import com.melly.springboot.mapper.UserMapper;
import com.melly.springboot.model.User;
import com.melly.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
