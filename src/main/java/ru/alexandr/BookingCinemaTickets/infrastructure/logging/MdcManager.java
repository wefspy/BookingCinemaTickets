package ru.alexandr.BookingCinemaTickets.infrastructure.logging;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.HttpRequestUtils;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.SecurityUtils;

@Component
public class MdcManager {

    public void trySetupMainMdc() {
        tryPutTraceId();
        tryPutUserId();
    }

    public void trySetupHttpMdc(HttpServletRequest request) {
        tryPutSessionId(request);
        tryPutClientIp(request);
    }

    public void clearMdc() {
        MDC.clear();
    }

    private boolean tryPutSessionId(HttpServletRequest request) {
        if (isBlankMdc(MdcKey.SESSION_ID)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                MDC.put(MdcKey.SESSION_ID, session.getId());
                return true;
            }
        }
        return false;
    }

    private boolean tryPutClientIp(HttpServletRequest request) {
        if (isBlankMdc(MdcKey.CLIENT_IP)) {
            String ip = HttpRequestUtils.getClientIp(request);
            MDC.put(MdcKey.CLIENT_IP, ip);
            return true;
        }
        return false;
    }

    private boolean tryPutTraceId() {
        if (isBlankMdc(MdcKey.TRACE_ID)) {
            MDC.put(MdcKey.TRACE_ID, TraceIdGenerator.generateTraceId());
            return true;
        }
        return false;
    }

    private boolean tryPutUserId() {
        if (isBlankMdc(MdcKey.USER_ID)) {
            SecurityUtils.getCurrentUser().ifPresent(user ->
                    MDC.put(MdcKey.USER_ID, user.getId().toString())
            );
            return true;
        }
        return false;
    }

    private boolean isBlankMdc(String key) {
        String val = MDC.get(key);
        return StringUtils.isBlank(val);
    }
}
