package com.how2java.tmall.dao;
  
import java.util.List;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
 
public interface ProductDAO extends JpaRepository<Product,Integer>{
    Page<Product> findByCategory(Category category, Pageable pageable);
    //新增一个通过分类查询所有产品的方法，因为这里不需要分页。
    List<Product> findByCategoryOrderById(Category category);
    //增加根据名称进行模糊查询的方法
    List<Product> findByNameLike(String keyword, Pageable pageable);
}