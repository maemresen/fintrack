package com.maemresen.fintrack.api.aspects;

import com.maemresen.fintrack.api.exceptions.InvalidParameterException;
import com.maemresen.fintrack.api.exceptions.ServiceException;
import com.maemresen.fintrack.api.exceptions.UnexpectedException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BusinessMethodAspect {

    @Pointcut("@annotation(com.maemresen.fintrack.api.aspects.annotations.BusinessMethod)")
    public void businessMethod() {
        // This pointcut expression targets all methods with @BusinessMethod
    }

    @AfterThrowing(pointcut = "businessMethod()", throwing = "throwable")
    public void afterThrowingException(Throwable throwable) throws Throwable {
        if (throwable instanceof ServiceException) {
            throw throwable;
        }

        if (throwable instanceof ConstraintViolationException constraintViolationException) {
            throw new InvalidParameterException(constraintViolationException.getMessage(), constraintViolationException);
        }

        log.warn("{} exception is not allowed to thrown by service, Converting into ServiceException", throwable.getClass());
        throw new UnexpectedException(throwable);
    }
}


