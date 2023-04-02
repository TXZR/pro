package com.example.pro.config;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class TywRestrictAspectHadler {
    private static Cache<String, Object> HTTP_CACHE = Caffeine.newBuilder()
            // 设置最后一次写入或访问后经过固定时间过期
            .expireAfterWrite(5, TimeUnit.MINUTES)
            // 初始的缓存空间大小
            .initialCapacity(100)
            // 缓存的最大条数
            .maximumSize(1000)
            .build();
    @Around(value = "@annotation(com.example.pro.config.TywRestrict) && @annotation(tywRestrict)",
            argNames = "joinPoint,tywRestrict")
    public Object cachePut(ProceedingJoinPoint joinPoint, TywRestrict tywRestrict) throws Throwable {
        Object target = joinPoint.getTarget();
        Class clazz = target.getClass();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        //先尝试从缓存中拿
        String cacheKey = String.format("%s-%s-%s", clazz.getSimpleName(), method.getName(), JSON.toJSONString(args));
        Object value = HTTP_CACHE.getIfPresent(cacheKey);
        if(value != null) {
            return value;
        }
        //缓存中没有，调实际的方法，需要限制次数  1.生成令牌  2.expiringmap
        value = joinPoint.proceed();
        HTTP_CACHE.put(cacheKey, value);
        return value;
    }
}
