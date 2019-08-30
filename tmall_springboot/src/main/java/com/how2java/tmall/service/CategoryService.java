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
	
	public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =categoryDAO.findAll(pageable);
 
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

	public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }
}
