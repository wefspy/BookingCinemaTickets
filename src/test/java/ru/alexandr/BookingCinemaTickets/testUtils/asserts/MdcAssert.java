package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.Assertions;
import org.slf4j.MDC;
import ru.alexandr.BookingCinemaTickets.infrastructure.logging.MdcKey;

public class MdcAssert {
    protected MdcAssert() {
    }

    public static MdcAssert assertThat() {
        return new MdcAssert();
    }

    public MdcAssert traceIdIsEqualsTo(String traceId) {
        Assertions.assertThat(MDC.get(MdcKey.TRACE_ID)).isEqualTo(traceId);
        return this;
    }

    public MdcAssert userIdIsEqualsTo(String userId) {
        Assertions.assertThat(MDC.get(MdcKey.USER_ID)).isEqualTo(userId);
        return this;
    }

    public MdcAssert clientIpIsEqualsTo(String clientIp) {
        Assertions.assertThat(MDC.get(MdcKey.CLIENT_IP)).isEqualTo(clientIp);
        return this;
    }

    public MdcAssert sessionIdIsEqualsTo(String sessionId) {
        Assertions.assertThat(MDC.get(MdcKey.SESSION_ID)).isEqualTo(sessionId);
        return this;
    }
}
