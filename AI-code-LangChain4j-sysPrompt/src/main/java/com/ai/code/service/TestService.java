package com.ai.code.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Tool("根据用户名查询对应用户的数量")
    public Integer getUserNameCount(@P("姓名") String name) {
        System.out.println("根据用户名查询对应用户的数量。");
        return 16;
    }

    @Tool("根据用户名查询用户的详情")
    public String getUserNameInfo(@P("姓名") String name) {
        System.out.println("根据用户名查询用户的详情。");
        return name + "的年龄是18，男，来自中国。";
    }
}
