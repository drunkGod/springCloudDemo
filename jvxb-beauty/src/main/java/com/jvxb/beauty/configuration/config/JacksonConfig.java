package com.jvxb.beauty.configuration.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * mvc序列化Jackson 配置
 */
@Configuration
public class JacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        // simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        // simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        /**
         * 默认日期格式， 需要其他格式，在对应的字段上配置@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
         */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(formatter);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
