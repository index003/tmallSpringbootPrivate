package com.how2java.tmall.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
 
import com.how2java.tmall.dao.UserDAO;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.util.Page4Navigator;
 
//提供list方法，进行分页查询

@Service
public class UserService {
     
    @Autowired UserDAO userDAO;
    
    //新增isExist(String name)的实现，判断某个名称是否已经被使用过了
    public boolean isExist(String name) {
        User user = getByName(name);
        return null!=user;
    }
 
    public User getByName(String name) {
        return userDAO.findByName(name);
    }
    
    //登录
    public User get(String name, String password) {
        return userDAO.getByNameAndPassword(name,password);
    }
 
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
 
    public void add(User user) {
        userDAO.save(user);
    }
}