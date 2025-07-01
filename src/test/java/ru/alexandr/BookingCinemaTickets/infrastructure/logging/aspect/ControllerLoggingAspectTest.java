package ru.alexandr.BookingCinemaTickets.infrastructure.logging.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.MdcManager;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.HttpRequestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerLoggingAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private HttpServletRequest request;
    @Mock
    private MdcManager mdcManager;

    private ControllerLoggingAspect aspect;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;
    private final String expectedResult = "welcome";

    @BeforeEach
    void setUp() {
        aspect = new ControllerLoggingAspect(mdcManager);

        logger = (Logger) LoggerFactory.getLogger(this.getClass());
        logger.setLevel(Level.DEBUG);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void logHttpRequest_ShouldLogging_WhenMethodReturnValue() throws Throwable {
        when(joinPoint.getTarget()).thenReturn(new TestController());
        when(joinPoint.proceed()).thenReturn(expectedResult);

        try (MockedStatic<HttpRequestUtils> requestUtilsMock = Mockito.mockStatic(HttpRequestUtils.class)) {
            requestUtilsMock.when(HttpRequestUtils::getCurrentHttpRequest).thenReturn(Optional.of(request));

            Object result = aspect.logHttpRequest(joinPoint);

            Assertions.assertThat(result).isEqualTo(expectedResult);
            List<ILoggingEvent> logsList = listAppender.list;
            Assertions.assertThat(logsList).isNotEmpty();

            verify(mdcManager).trySetupMain();
            verify(mdcManager).trySetupHttp(request);
            verify(mdcManager).clearAll();
        }
    }

    @Test
    void logHttpRequest_ShouldLogging_WhenMethodThrowException() throws Throwable {
        Exception exception = new Exception();
        when(joinPoint.getTarget()).thenReturn(new TestController());
        when(joinPoint.proceed()).thenThrow(exception);

        try (MockedStatic<HttpRequestUtils> requestUtilsMock = Mockito.mockStatic(HttpRequestUtils.class)) {
            requestUtilsMock.when(HttpRequestUtils::getCurrentHttpRequest).thenReturn(Optional.of(request));

            Assertions.assertThatThrownBy(() -> aspect.logHttpRequest(joinPoint))
                    .isInstanceOf(exception.getClass());

            List<ILoggingEvent> logsList = listAppender.list;
            Assertions.assertThat(logsList).isNotEmpty();

            verify(mdcManager).trySetupMain();
            verify(mdcManager).trySetupHttp(request);
        }
    }

    class TestController {}
}