package com.firespread.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description: Swaggerui配置
 * @author: DelucaWu
 * @createDate: 2022/5/10 20:19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //进入swagger-ui信息
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("firespread")
                .version("v1.0")
                .description("福州大学物信学院林火蔓延项目接口")
                .contact(new Contact("DelucaWu", "https://blog.csdn.net/djhpa", "1620584767@qq.com"))
                .build();
    }

    @Bean
    public Docket createRestApi(){
       return new Docket(DocumentationType.SWAGGER_2)
               .apiInfo(apiInfo())
               .select()
                //对所有api进行监控
               .apis(RequestHandlerSelectors.any())
               // 不显示错误的接口地址
               .paths(Predicates.not(PathSelectors.regex("/error.*")))
               // 对根下所有路径进行监控
               .paths(PathSelectors.regex("/.*"))
               .build();

    }
}
