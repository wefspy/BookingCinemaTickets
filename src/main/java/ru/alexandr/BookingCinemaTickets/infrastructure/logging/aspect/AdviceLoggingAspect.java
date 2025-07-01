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
public class AdviceLoggingAspect {
    private final MdcManager mdcManager;

    public AdviceLoggingAspect(MdcManager mdcManager) {
        this.mdcManager = mdcManager;
    }

    @Around("within(@org.springframework.web.bind.annotation.ControllerAdvice *) " +
            "|| within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    public Object logExceptionHandling(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        HttpServletRequest request = HttpRequestUtils.getCurrentHttpRequest().orElseThrow();

        mdcManager.trySetupMainMdc();
        mdcManager.trySetupHttpMdc(request);

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
                    request.getMethod(), request.getRequestURI(), stopWatch.getTotalTimeMillis(),
                    e.getClass().getName(), e.getMessage());
            throw e;
        } finally {
            mdcManager.clearMdc();
        }
    }
} 