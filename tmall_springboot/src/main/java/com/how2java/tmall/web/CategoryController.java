package com.how2java.tmall.web;
 
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page4Navigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

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
    
    /*
            提供增加方法add.
    1. 首选通过CategoryService 保存到数据库
    2. 然后接受上传图片，并保存到 img/category目录下
    3. 文件名使用新增分类的id
    4. 如果目录不存在，需要创建
    5. image.transferTo 进行文件复制
    6. 调用ImageUtil的change2jpg 进行文件类型强制转换为 jpg格式
    7. 保存图片

           注： 这里list和add对应的映射路径都是 categories，
    但是一个是 GetMapping一个是 PostMapping，REST 规范就是通过method的区别来辨别到底是获取还是增加的。
    
*/    
    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }
    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }
    
    /*
            增加delete函数，映射 ListCategory.html的 ajax 请求：
    @DeleteMapping("/categories/{id}")
    
    1. 首先根据id 删除数据库里的数据
    2. 删除对应的文件
    3. 返回 null, 会被RESTController 转换为空字符串。
    */
    
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        categoryService.delete(id);
        File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }
    
    //提供 get 方法，把id对应的Category取出来，并转换为json对象发给浏览器
    
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        Category bean=categoryService.get(id);
        return bean;
    }
    
    /*
            增加update函数，它是用 PutMapping 来映射的，因为 REST要求修改用PUT来做。
    1. 获取参数名称
            这里获取参数用的是 request.getParameter("name"). 为什么不用 add里的注入一个 Category对象呢？ 因为。。。PUT 方式注入不了。。。 
            只能用这种方式取参数了，试了很多次才知道是这么个情况~
    2. 通过 CategoryService 的update方法更新到数据库
    3. 如果上传了图片，调用增加的时候共用的 saveOrUpdateImageFile 方法。
    4. 返回这个分类对象。
    */
    
    @PutMapping("/categories/{id}")
    public Object update(Category bean, MultipartFile image,HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);
 
        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return bean;
    }
}