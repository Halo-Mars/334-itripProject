package com.bdqn.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextFlt {
    public static void main(String[] args) throws IOException, TemplateException {
        entity entity=new entity(1,"ctt");
        entity entity1=new entity(2,"xz");
        List<entity> list=new ArrayList<>();
        list.add(entity);
        list.add(entity1);



        Map<String,Object> map=new HashMap<>();
        map.put("li",list);

        Configuration configuration
                =new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setDirectoryForTemplateLoading(new File("D:\\Users\\Administrator\\itrip-project\\itripbackend\\itripauth\\src\\main\\resources"));

        Template template=configuration.getTemplate("Text1.flt");

        //传递数据
        template.process(map,new FileWriter("D:\\a.txt"));

    }
}
