package com.ai.code.config;

import com.ai.code.common.Order;
import com.ai.code.common.Product;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StructuredOutputConfig {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    public static class ProductOutputConverter extends BeanOutputConverter<Product> {
        public ProductOutputConverter() {
            super(Product.class);
        }
    }

    public static class OrderBeanOutputConverter extends BeanOutputConverter<Order> {
        public OrderBeanOutputConverter() {
            super(Order.class);
        }
    }

    @Bean
    public BeanOutputConverter<Product> productOutputConverter() {
        return new ProductOutputConverter();
    }

    @Bean
    public BeanOutputConverter<Order> orderBeanOutputConverter(){
        return new OrderBeanOutputConverter();
    }
}
