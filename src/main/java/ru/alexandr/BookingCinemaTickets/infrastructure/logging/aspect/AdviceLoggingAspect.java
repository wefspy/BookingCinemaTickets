package ru.alexandr.BookingCinemaTickets.infrastructure.logging.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.MdcKey;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.TraceIdGenerator;

import java.util.Optional;

@Aspect
@Component
public class AdviceLoggingAspect {

    @Around("within(@org.springframework.web.bind.annotation.ControllerAdvice *) " +
            "|| within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    public Object logExceptionHandling(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        HttpServletRequest request = getCurrentHttpRequest().orElseThrow();

        String existingTraceId = MDC.get(MdcKey.TRACE_ID);
        if (existingTraceId == null) {
            MDC.put(MdcKey.TRACE_ID, TraceIdGenerator.generateTraceId());
        }

        logger.info("Exception Handler - Method: [{}] Path: [{}] Handler: [{}]",
                request.getMethod(), request.getRequestURI(), joinPoint.getSignature().getName());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            logger.info("Exception Handled - Method: [{}] Path: [{}] - Completed in {} ms",
                    request.getMethod(), request.getRequestURI(), stopWatch.getTotalTimeMillis());

            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("Exception Handler Failed - Method: [{}] Path: [{}] - Failed after {} ms with exception: {}, message: {}",
                    request.getMethod(), request.getRequestURI(),
                    stopWatch.getTotalTimeMillis(), e.getClass().getName(), e.getMessage());
            throw e;
        } finally {
            MDC.remove(MdcKey.TRACE_ID);
        }
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }
} 