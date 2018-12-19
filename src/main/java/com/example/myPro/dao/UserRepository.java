package com.example.myPro.dao;

import com.example.myPro.model.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

//public interface UserRepository extends JpaRepository<User, Long> {
public interface UserRepository {
    User findByUserName(String userName);

    User findByUserNameOrEmail(String username, String email);
}
