package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class IndexController {
    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(){
        return "HelloWorld!";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") int groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") String type,
                          @RequestParam(value = "key", defaultValue = "1") String key){
        return String.format("GroupId{%d}, UserId{%d}, Type{%s}, Key{%s}", groupId, userId, type, key);
    }

    @RequestMapping(path = {"/vm"})
    public String news(Model model){
        model.addAttribute("name", "WSM&FX");
        model.addAttribute("value1", "123");

        List<String> colors = Arrays.asList(new String[]{"Red", "Green", "Blue"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<4; i++){
            map.put(String.valueOf(i), String.valueOf(i*i*i));
        }
        model.addAttribute("map", map);

        return "news";
    }

}
