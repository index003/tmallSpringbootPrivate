package com.how2java.tmall.service;
 
import com.how2java.tmall.dao.OrderDAO;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

		/*
		OrderService,提供分页查询。
		还提供了 订单状态的常量，Order.java 的 getStatusDesc 会用到。
		另外还提供了一个奇怪的方法，removeOrderFromOrderItem，它的作用是把订单里的订单项的订单属性设置为空。。。
		听上去绕口吧。 再用代码说一下，比如有个 order, 拿到它的 orderItems， 然后再把这些orderItems的order属性，设置为空。
		为什么要做这个事情呢？ 因为SpringMVC ( springboot 里内置的mvc框架是 这个东西)的 RESTFUL 注解，
		在把一个Order转换为json的同时，会把其对应的 orderItems 转换为 json数组， 而 orderItem对象上有 order属性，
		 这个order 属性又会被转换为 json对象，然后这个 order 下又有 orderItems 。。。 
		就这样就会产生无穷递归，系统就会报错了。
		所以这里采用 removeOrderFromOrderItem 把 OrderItem的order设置为空就可以了。
		那么为什么不用 @JsonIgnoreProperties 来标记这个字段呢？ 因为后续我们要整合Redis，
		如果标记成了 @JsonIgnoreProperties 会在和 Redis 整合的时候有 Bug,
		 所以还是采用这种方式比较好。 这些都是站长掉进去的坑~
		*/
 
@Service
public class OrderService {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";  
     
    @Autowired OrderDAO orderDAO;
    @Autowired OrderItemService orderItemService;
     
    public Page4Navigator<Order> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =orderDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
 
    public void removeOrderFromOrderItem(List<Order> orders) {
        for (Order order : orders) {
            removeOrderFromOrderItem(order);
        }
    }
 
    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItems= order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(null);
        }
    }
 
    public Order get(int oid) {
        return orderDAO.findOne(oid);
    }
 
    public void update(Order bean) {
        orderDAO.save(bean);
    }
    
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> ois) {
        float total = 0;
        add(order);
 
        if(false)
            throw new RuntimeException();
 
        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }
    public void add(Order order) {
        orderDAO.save(order);
    }
 
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }
 
    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }
    
    public void cacl(Order o) {
        List<OrderItem> orderItems = o.getOrderItems();
        float total = 0;
        for (OrderItem orderItem : orderItems) {
            total+=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        o.setTotal(total);
    }
}