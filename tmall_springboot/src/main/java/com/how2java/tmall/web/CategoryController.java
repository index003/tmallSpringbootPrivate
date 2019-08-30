package com.how2java.tmall.web;
 
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.Page4Navigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import java.util.List;

		/*
		 * 这个就是专门用来提供 RESTFUL 服务器控制器了
		
		@RestController
		表示这是一个控制器，并且对每个方法的返回值都会直接转换为 json 数据格式。
		 
		@Autowired CategoryService categoryService;
		自动装配 CategoryService
		 
			@GetMapping("/categories")
		    public List<Category> list() throws Exception {
		    	return categoryService.list();
		    }
		
		对于categories 访问，会获取所有的 Category对象集合，并返回这个集合。
		 因为是声明为 @RestController， 所以这个集合，又会被自动转换为 JSON数组抛给浏览器。
		 
		 */

  
@RestController
public class CategoryController {
    @Autowired CategoryService categoryService;
     
    @GetMapping("/categories")
    /*public List<Category> list() throws Exception {
        return categoryService.list();
    }*/
    
		/*
		 CategoryController 修改原 list 方法，接受 start 和 size 参数。
		    业务逻辑上就调用 CategoryService 的新的 list 方法了。
		    然后返回的是 Page4Navigator 类型，并通过 RestController 转换为 json 对象抛给浏览器。
		 */
    
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Category> page =categoryService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }
}