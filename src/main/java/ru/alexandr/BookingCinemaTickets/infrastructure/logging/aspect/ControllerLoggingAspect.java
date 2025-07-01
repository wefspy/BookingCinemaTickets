package ru.alexandr.BookingCinemaTickets.infrastructure.logging.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.MdcManager;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.HttpRequestUtils;

@Aspect
@Component
public class ControllerLoggingAspect {
    private final MdcManager mdcManager;

    public ControllerLoggingAspect(MdcManager mdcManager) {
        this.mdcManager = mdcManager;
    }


    @Around("within(@org.springframework.stereotype.Controller *) " +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        HttpServletRequest request = HttpRequestUtils.getCurrentHttpRequest().orElseThrow();

        mdcManager.trySetupMainMdc();
        mdcManager.trySetupHttpMdc(request);

        logger.info("HTTP Request - Method: [{}] Path: [{}]",
                request.getMethod(), request.getRequestURI());
        if (logger.isDebugEnabled()) {
            logger.debug("Headers: {}", HttpRequestUtils.getHeadersAsString(request));
            logger.debug("Query Parameters: {}", request.getQueryString());
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            logger.info("HTTP Response - Method: [{}] Path: [{}] - Completed in {} ms",
                    request.getMethod(), request.getRequestURI(), stopWatch.getTotalTimeMillis());

            mdcManager.clearMdc();

            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            logger.error("HTTP Error - Method: [{}] Path: [{}] - Failed after {} ms with exception: {}, message: {}",
                    request.getMethod(), request.getRequestURI(), stopWatch.getTotalTimeMillis(),
                    e.getClass().getName(), e.getMessage());
            throw e;
        }
    }
}