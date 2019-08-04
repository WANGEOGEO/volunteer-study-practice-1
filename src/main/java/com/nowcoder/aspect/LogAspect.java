package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by nowcoder on 2016/6/26.
 */
@Aspect
//加了Component才会初始化
@Component
public class LogAspect {
    //记录一些log
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //括号里意思：执行任何方法
    //所以总的来说这个就是：在执行人和方法之前都要执行这个。正则表达式的说法，*就是匹配一切
    //jointPoint就是切点，面对切面编程的概念。
    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            //这里可以打印出触发了这个东西的method里面的参数。
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method: " + sb.toString());
    }

    //同上的意思
    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("after method: ");
    }
}
