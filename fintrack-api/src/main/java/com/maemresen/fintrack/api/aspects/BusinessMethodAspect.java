package com.maemresen.fintrack.api.aspects;

import com.maemresen.fintrack.api.exceptions.BusinessException;
import com.maemresen.fintrack.api.exceptions.InvalidParameter;
import com.maemresen.fintrack.api.exceptions.ServiceException;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BusinessMethodAspect {

    @Pointcut("@annotation(com.maemresen.fintrack.api.aspects.annotations.BusinessMethod)")
    public void businessMethod() {
        // This pointcut expression targets all methods with @BusinessMethod
    }

    @AfterThrowing(pointcut = "businessMethod()", throwing = "throwable")
    public void afterThrowingException(Throwable throwable) throws Throwable {
        if (throwable instanceof BusinessException) {
            throw throwable;
        }

        if (throwable instanceof ConstraintViolationException constraintViolationException) {
            throw new InvalidParameter(constraintViolationException.getMessage(), constraintViolationException);
        }

        throw new ServiceException(throwable.getMessage(), throwable);
    }
}
