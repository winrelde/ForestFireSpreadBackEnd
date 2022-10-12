package com.firespread.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
public class DruidConfig {
     /*
       将自定义的 Druid数据源添加到容器中，不再让 Spring Boot 自动创建
       绑定全局配置文件中的 druid 数据源属性到 com.alibaba.druid.pool.DruidDataSource从而让它们生效
       @ConfigurationProperties(prefix = "spring.datasource")：作用就是将 全局配置文件中
       前缀为 spring.datasource的属性值注入到 com.alibaba.druid.pool.DruidDataSource 的同名参数中
     */
     @Bean(name="primaryDataSource")
     @Qualifier("primaryDataSource")
     @Primary
     @ConfigurationProperties(prefix = "spring.datasource.primary")
     public DataSource getDataSource(){
          return new DruidDataSource();
     }

     /**
      * 配置德鲁伊监控
      * */
     //1-配置一个管理后台的Sevlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
         //注册ServletBean
         /**
          * "/druid/*":表示处理Druid下的所有数据请求
          * */
        ServletRegistrationBean bean=new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //设置后台登录的账号密码
        HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "123456");
        bean.setInitParameters(initParameters);//初始化参数
        return bean;
    }

    ////1-配置一个监控的ilter
    public FilterRegistrationBean webStatFilter(){
        //注册 FilterBean
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        //可以过滤那些请求
        HashMap<String, String> initParameters = new HashMap<>();
        //放行静态资源文件与Druid请求
        initParameters.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParameters);//初始化参数
        bean.setUrlPatterns(Arrays.asList("/*"));//拦截所有请求
        return bean;
    }
}
