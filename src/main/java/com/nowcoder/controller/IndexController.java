package com.nowcoder.controller;

import com.nowcoder.aspect.LogAspect;
import com.nowcoder.model.User;
import com.nowcoder.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by nowcoder on 2016/6/26.
 */
/**
本质上来说，这个java文件里面提到的事情，都是对应用户请求的不同界面来进行返回response，其中可以选择机械化模板返回，也可以使用自定义.vm返回。
 */
//@Controller
public class IndexController {
    //logger就是用来记录的。
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    //autowired的意思就是，每次只要系统启动，就会有这么一个东西被创建好，并且赋值到这个成员函数上。
    //本质上这个Autowired关键字起到的是一个注入的效果。类与类之间并没有继承，只有互相使用。
    @Autowired
    private ToutiaoService toutiaoService;

    //mapping an HTTP request to a method
    //path可以支持多个变量
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    //因为返回的是一个string，所以我还要告诉他返回的是一个body
    @ResponseBody
    public String index(HttpSession session) {
        //往logger里面加东西。
        logger.info("Visit Index");
        //把Session中的msg这个attribute取了出来。这个原本是楼下那个redirect拿来演示用的。
        return "Hello NowCoder," + session.getAttribute("msg")
                + "<br> Say:" + toutiaoService.say();
    }

    //这里的groupId和userId都是变量，之后可以有办法和他联动。
    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          //这里后面request返回的参数，就是链接里面?之后的东西，谁=几这样子的。
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key) {
        //可以return出来上面我们标号的variable
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}", groupId, userId, type, key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model) {
        //这个model的意思啊，就是这是个后端和前端渲染之间可以相互联系的一个桥梁。
        model.addAttribute("value1", "vv1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});

        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }

        model.addAttribute("colors", colors);
        model.addAttribute("map", map);
        model.addAttribute("user", new User("Jim"));

        //这里这个return “news”，他会自动去templates里面找哪里有一个叫news.vm的一个文件。
        //.vm文件一般我们称呼他为模板文件。
        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        //用while读取全部header，比如什么host,connection,accept,user-agent什么的，甚至还有一个cookie的sessionID。
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            //然后全部加入到我们最开始建立的StringBuilder之中了
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        //接下来，认真地把全部的cookie都摸出来。但这个例子里面是只有一个cookie拉，所以和上面的里面的cookie没差。
        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }

        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        //？后面的就是queryString
        sb.append("getQueryString:" + request.getQueryString() + "<br>");
        //从服务器根目录以后的都是URI
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");

        return sb.toString();

    }

    //写一个单独的method来往response里面加东西。
    @RequestMapping(value = {"/response"})
    @ResponseBody
    //在这里，不要怀疑，response的后面括号里面方的依然是传进来的东西，别想别的。包括response，也是一个穿进来的东西。
    //而我们的任务，就是把我们要改的东西都改好，然后给他传回去。包括下发新的cookie。
    public String response(@CookieValue(value = "nowcoderid", defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "NowCoderId From Cookie:" + nowcoderId;
    }

    //这里有提到一个301和302的区别，301是永久重定向跳转，浏览器跳过一次之后，就会记住下次也要跳，下次再访问这个页面，直接会跳到重定向页面。302的话不是。
    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession session) {
        //注释里面的也是一种写法，比较直观。
        /*
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;*/
        //下面这个写法，redirect：是一个固定前缀，可以触发跳转。这个办法写的话，全部都是302跳转。
        session.setAttribute("msg", "Jump from redirect.");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }

    //自己定义了一个Exception Handler
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
