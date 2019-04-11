package com.melly.springboot.service;

import com.melly.springboot.model.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();
}
