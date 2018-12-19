package com.example.myPro.controller;

import com.example.myPro.dao.UserDao;
import com.example.myPro.model.database.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/getUsers")
    public List<UserEntity> getUsers() {
        List<UserEntity> users = userDao.getAll();
        return users;
    }

    @RequestMapping("/getUser")
    public UserEntity getUser(Long id) {
        UserEntity user = userDao.getOne(id);
        return user;
    }

    @RequestMapping("/add")
    public void save(UserEntity user) {
        userDao.insert(user);
    }

    @RequestMapping(value = "update")
    public void update(UserEntity user) {
        userDao.update(user);
    }

    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userDao.delete(id);
    }
}
