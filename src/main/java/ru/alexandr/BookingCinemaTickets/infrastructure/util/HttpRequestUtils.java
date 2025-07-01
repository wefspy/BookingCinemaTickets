package ru.alexandr.BookingCinemaTickets.infrastructure.util;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public final class HttpRequestUtils {
    private static final String headerNameSourceIp = "X-Forwarded-For";

    private HttpRequestUtils() {}

    public static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    public static String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames()
                .asIterator()
                .forEachRemaining(headerName ->
                        headers.append(headerName)
                                .append(": ")
                                .append(request.getHeader(headerName)).append(", "));
        return headers.toString();
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader(headerNameSourceIp);
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
