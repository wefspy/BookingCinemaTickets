package ru.alexandr.BookingCinemaTickets.infrastructure.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class ComponentLoggingAspect {
    
    @Around("within(@org.springframework.stereotype.Component *)")
    public Object logComponentMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(methodSignature.getDeclaringType());

        String methodName = methodSignature.getName();

        if (logger.isDebugEnabled()) {
            String arguments = Arrays.toString(joinPoint.getArgs());
            logger.debug("Component Method - {}.{} called with arguments: {}",
                    methodSignature.getDeclaringType().getSimpleName(), methodName, arguments);
        }
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Component Return - {}.{} completed in {} ms",
                        methodSignature.getDeclaringType().getSimpleName(), methodName,
                        stopWatch.getTotalTimeMillis());
            }
            
            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("Component Error - {}.{} failed after {} ms with exception: {}, message: {}",
                    methodSignature.getDeclaringType().getSimpleName(), methodName,
                    stopWatch.getTotalTimeMillis(), e.getClass().getName(), e.getMessage());
            throw e;
        }
    }
} 