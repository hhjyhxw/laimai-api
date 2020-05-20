package com.icloud.config.interceptor;

import com.icloud.config.global.MyPropertitys;
import com.icloud.config.resolver.LoginUserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
class InterceptorsStack implements WebMvcConfigurer {

    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
    @Autowired
    private ApiLoginInterceptor apiLoginInterceptor;
    @Autowired
	private MyPropertitys myPropertitys;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new XssCsrfInterceptor()).addPathPatterns(new String[] { "/**" }).excludePathPatterns(new String[] { "/thirdInterfacePath/**" })
        //本地
        registry.addInterceptor(apiLoginInterceptor).addPathPatterns(new String[] { "/api/**" });//api访问拦截


    }

    /**
     * 参数处理拦截
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }

    /**
     * 静态资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    /**
     * 设置全局编码
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

}
