package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2016/6/26.
 */
//只要定义成Service，那么每次系统起来，Spring都会主动去创建这么一个对象。
@Service
public class ToutiaoService {
    public String say() {
        return "This is from ToutiaoService";
    }
}
