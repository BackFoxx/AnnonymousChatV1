package toyproject.annonymouschat.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
@Slf4j
public class ArgumentResolverV1 implements MethodInterceptor {
    private final Object target;

    public ArgumentResolverV1(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("ArgumentResolver Proxy 실행");
        return proxy.invoke(target, args);
    }
}
