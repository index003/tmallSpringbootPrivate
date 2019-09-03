package com.how2java.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;

		/*
		 * @Service
		标记这个类是 Service类
		 
		@Autowired CategoryDAO categoryDAO;
		自动装配 上个步骤的 CategoryDAO 对象
		 
			public List<Category> list() {
		    	Sort sort = new Sort(Sort.Direction.DESC, "id");
				return categoryDAO.findAll(sort);
			}
		首先创建一个 Sort 对象，表示通过 id 倒排序， 然后通过 categoryDAO进行查询。 
		
		注： 这里抛弃了 CategoryService 接口 加上 CategoryService 实现类的这种累赘的写法，而是直接使用 CategoryService 作为实现类来做。
		
		*/



@Service
public class CategoryService {
	@Autowired CategoryDAO categoryDAO;
	
	//分页功能
	public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =categoryDAO.findAll(pageable);
 
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
	
	//列表展示
	public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }
	
	//新增功能
	public void add(Category bean) {
        categoryDAO.save(bean);
    }
	
	//删除功能
    public void delete(int id) {
        categoryDAO.delete(id);
    }
    
    //编辑功能
    public Category get(int id) {
        Category c= categoryDAO.findOne(id);
        return c;
    }
    
    //修改功能
    public void update(Category bean) {
        categoryDAO.save(bean);
    }
    
	 /*
	    增加两个 removeCategoryFromProduct 方法。
	    这个方法的用处是删除Product对象上的 分类。 为什么要删除呢？ 因为在对分类做序列还转换为 json 的时候
	    ，会遍历里面的 products, 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了，就搞死个人了
	    而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系。
	   和后台订单管理哪里去掉 orderItem 上的 订单信息道理是相同的。
	*/    
    
    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category : cs) {
            removeCategoryFromProduct(category);
        }
    }
    
    public void removeCategoryFromProduct(Category category) {
        List<Product> products =category.getProducts();
        if(null!=products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }
         List<List<Product>> productsByRow =category.getProductsByRow();
        if(null!=productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p: ps) {
                    p.setCategory(null);
                }
            }
        }
    }
    
}

