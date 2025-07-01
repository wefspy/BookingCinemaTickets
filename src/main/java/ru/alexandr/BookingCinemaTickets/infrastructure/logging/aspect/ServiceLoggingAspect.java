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
public class ServiceLoggingAspect {
    
    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        
        String methodName = methodSignature.getName();
        String className = joinPoint.getTarget().getClass().getName();

        if (logger.isDebugEnabled()) {
            String arguments = Arrays.toString(joinPoint.getArgs());
            logger.info("Service Method - {}.{}({}) called", className, methodName, arguments);
        } else {
            logger.info("Service Method - {}.{} called", className, methodName);
        }
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Service Return - {}.{} completed in {} ms with result: {}",
                        className, methodName, stopWatch.getTotalTimeMillis(), result);
            } else {
                logger.info("Service Return - {}.{} completed in {} ms",
                        className, methodName, stopWatch.getTotalTimeMillis());
            }
            
            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("Service Error - {}.{} failed after {} ms with exception: {}, message: {}",
                    className, methodName, stopWatch.getTotalTimeMillis(), e.getClass().getName(), e.getMessage());
            throw e;
        }
    }
} 