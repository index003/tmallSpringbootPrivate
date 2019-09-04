package com.how2java.tmall.dao;
  
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.User;

		/*		
		OrderItem DAO类，另外还提供过了通过订单查询的方法
		List<OrderItem> findByOrderOrderByIdDesc(Order order);
		这种方式在命名里提供OrderByIdDesc，就进行到排序了，就可以不用传Sort参数了。两种方式，同学们都了解了解。
 		*/

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer>{
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
    //增加根据产品获取OrderItem的方法：
    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByUserAndOrderIsNull(User user);
    
}