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
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.TraceConstants;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.TraceIdGenerator;

import java.util.Optional;

@Aspect
@Component
public class ControllerLoggingAspect {

    @Around("within(@org.springframework.stereotype.Controller *) " +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        HttpServletRequest request = getCurrentHttpRequest().orElseThrow();

        String traceId = TraceIdGenerator.generateTraceId();
        MDC.put(TraceConstants.TRACE_ID_MDC_KEY, traceId);

        logger.info("HTTP Request - Method: [{}] Path: [{}] From: [{}]",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        if (logger.isDebugEnabled()) {
            logger.debug("Headers: {}", getHeadersAsString(request));
            logger.debug("Query Parameters: {}", request.getQueryString());
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            logger.info("HTTP Response - Method: [{}] Path: [{}] - Completed in {} ms",
                    request.getMethod(), request.getRequestURI(), stopWatch.getTotalTimeMillis());

            MDC.remove(TraceConstants.TRACE_ID_MDC_KEY);

            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("HTTP Error - Method: [{}] Path: [{}] - Failed after {} ms with exception: {}, message: {}",
                    request.getMethod(), request.getRequestURI(),
                    stopWatch.getTotalTimeMillis(), e.getClass().getName(), e.getMessage());
            throw e;
        }
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    private String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames()
                .asIterator()
                .forEachRemaining(headerName ->
                        headers.append(headerName)
                                .append(": ")
                                .append(request.getHeader(headerName)).append(", "));
        return headers.toString();
    }
}