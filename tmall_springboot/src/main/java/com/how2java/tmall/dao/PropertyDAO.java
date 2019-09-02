package com.how2java.tmall.dao;
  
import java.util.List;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;

	/*
	PropertyDAO 继承了 JpaRepository。提供常见的CRUD。
	
	除此之外，还提供了根据分类进行查询:
	Page<Property> findByCategory(Category category, Pageable pageable);
	
	注意看，这个是个接口，它是没有实现类的，至少我们是没有显式提供实现类。 
	那么要进行条件查询，就是在方法名上面做文章。 比如这里的findByCategory，就是基于Category进行查询，第二个参数传一个 Pageable ， 就支持分页了。
	这就是JPA所谓的不用写 SQL语句。。。因为需要的信息都在方法名和参数里提供了。
	*/

public interface PropertyDAO extends JpaRepository<Property,Integer>{
    Page<Property> findByCategory(Category category, Pageable pageable);
}