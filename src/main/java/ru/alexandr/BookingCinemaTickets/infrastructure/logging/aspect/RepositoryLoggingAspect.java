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
public class RepositoryLoggingAspect {

    @Around("execution(public !void org.springframework.data.repository.Repository+.*(..))")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        if (logger.isDebugEnabled()) {
            String arguments = Arrays.toString(joinPoint.getArgs());
            logger.debug("Repository Method - {}.{}({}) called", className, methodName, arguments);
        }
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            if (logger.isDebugEnabled()) {
                logger.debug("Repository Return - {}.{} completed in {} ms with result: {}",
                        className, methodName, stopWatch.getTotalTimeMillis(), result);
            }

            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("Repository Error - {}.{} failed after {} ms with exception: {}, message: {}",
                    methodSignature.getDeclaringType().getSimpleName(), methodName,
                    stopWatch.getTotalTimeMillis(), e.getClass().getName(), e.getMessage());
            throw e;
        }
    }
} 