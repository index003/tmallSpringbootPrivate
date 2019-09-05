package com.how2java.tmall.config;
 
import com.how2java.tmall.interceptor.LoginInterceptor;
import com.how2java.tmall.interceptor.OtherInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 
//配置LoginInterceptor这个拦截器

@Configuration
class WebMvcConfigurer extends WebMvcConfigurerAdapter{
     
	//追加 OtherInterceptor
    @Bean
    public OtherInterceptor getOtherIntercepter() {
        return new OtherInterceptor();
    }
    @Bean
    public LoginInterceptor getLoginIntercepter() {
        return new LoginInterceptor();
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getOtherIntercepter())
                .addPathPatterns("/**");
        registry.addInterceptor(getLoginIntercepter())
                .addPathPatterns("/**");
    }
}