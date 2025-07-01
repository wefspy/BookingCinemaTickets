package ru.alexandr.BookingCinemaTickets.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.HttpRequestUtils;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.SecurityUtils;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.MdcAssert;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MdcManagerUnitTest {
    @InjectMocks
    private MdcManager mdcManager;

    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @Test
    void trySetupMainMdc_ShouldPutMainMdc_WhenMainIsEmpty() {
        String userId = "1";
        String traceId = "2";

        when(userDetails.getId()).thenReturn(Long.valueOf(userId));

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class);
             MockedStatic<TraceIdGenerator> traceIdGeneratorMock = mockStatic(TraceIdGenerator.class)
        ) {
            securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(Optional.of(userDetails));
            traceIdGeneratorMock.when(TraceIdGenerator::generateTraceId).thenReturn(traceId);

            mdcManager.trySetupMain();

            MdcAssert.assertThat().traceIdIsEqualsTo(traceId)
                    .userIdIsEqualsTo(userId);
        }
    }

    @Test
    void trySetupMainMdc_ShouldDoNoting_WhenMainIsNotBlank() {
        String savedUserId = "1";
        String savedTraceId = "2";

        MDC.put(MdcKey.USER_ID, savedUserId);
        MDC.put(MdcKey.TRACE_ID, savedTraceId);

        mdcManager.trySetupMain();

        MdcAssert.assertThat().userIdIsEqualsTo(savedUserId)
                .traceIdIsEqualsTo(savedTraceId);
    }

    @Test
    void trySetupHttpMdc_ShouldPutHttpMdc_WhenHttpIsEmpty() {
        String sessionId = "1";
        String clientIp = "2";
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(sessionId);

        try (MockedStatic<HttpRequestUtils> httpRequestUtilsMock = mockStatic(HttpRequestUtils.class)) {
            httpRequestUtilsMock.when(() -> HttpRequestUtils.getClientIp(request)).thenReturn(clientIp);

            mdcManager.trySetupHttp(request);

            MdcAssert.assertThat().clientIpIsEqualsTo(clientIp)
                    .sessionIdIsEqualsTo(sessionId);
        }
    }

    @Test
    void trySetupHttpMdc_ShouldDoNothing_WhenHttpIsNotBlank() {
        String savedClientIp = "1";
        String savedSessionId = "2";

        MDC.put(MdcKey.CLIENT_IP, savedClientIp);
        MDC.put(MdcKey.SESSION_ID, savedSessionId);

        mdcManager.trySetupHttp(request);

        MdcAssert.assertThat().clientIpIsEqualsTo(savedClientIp)
                .sessionIdIsEqualsTo(savedSessionId);
    }

    @Test
    void clear_All_ShouldDeleteEntry() {
        String userId = "1";
        MDC.put(MdcKey.USER_ID, userId);

        mdcManager.clearAll();

        Assertions.assertThat(MDC.get(MdcKey.USER_ID)).isNull();
    }
}