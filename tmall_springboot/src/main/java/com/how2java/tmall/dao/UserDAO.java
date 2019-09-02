package com.how2java.tmall.dao;
  
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.how2java.tmall.pojo.User;
 
//DAO， CRUD一套

public interface UserDAO extends JpaRepository<User,Integer>{
 
}