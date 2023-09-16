package com.maemresen.fintrack.webservice.api.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
@Slf4j
public class RestControllerLoggingAspect {

    private static final String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logRequestAndTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final var start = System.currentTimeMillis();
        final var result = proceedingJoinPoint.proceed();
        final var elapsedTime = System.currentTimeMillis() - start;
        final var requestInfo = getRequestInfo();
        log.info("Request processed successfully from IP {} using {} {} in {}ms",
                 requestInfo.ipAddress(),
                 requestInfo.httpMethod(),
                 requestInfo.uri(),
                 elapsedTime);
        return result;
    }

    @AfterThrowing(pointcut = "restControllerMethods()", throwing = "exception")
    public void logExceptions(Throwable exception) {
        final var requestInfo = getRequestInfo();
        log.error("Exception occurred while processing request from IP {} using {} {}: {}",
                  requestInfo.ipAddress(),
                  requestInfo.httpMethod(),
                  requestInfo.uri(),
                  exception.getMessage(),
                  exception);
    }

    private RequestInfo getRequestInfo() {
        final var request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        final var ipAddress = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR_HEADER)).orElseGet(request::getRemoteAddr);
        final var uri = request.getRequestURI();
        final var httpMethod = request.getMethod();
        return new RequestInfo(ipAddress, uri, httpMethod);
    }

    private record RequestInfo(String ipAddress, String uri, String httpMethod) {}
}
