package common.interceptor.http;

import bank_binding.common.interceptor.AuthenticateService;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HttpAuthenticationInterceptor extends HandlerInterceptorAdapter {
  private final AuthenticateService authenticateService;
  private final ImmutableMap<String, String> apiMapper;

  public HttpAuthenticationInterceptor(AuthenticateService authenticateService, Map<String, String> apiMapper) {
    this.authenticateService = authenticateService;
    this.apiMapper = ImmutableMap.copyOf(apiMapper);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    val api = request.getRequestURI();
    log.info("api={} header=[{}]", api, getHeader(request));
    val clientId = request.getHeader("client-id");
    val clientKey = request.getHeader("client-key");

    authenticateService.authenticate(clientId, clientKey, apiMapper.getOrDefault(api, ""));
    return true;
  }

  private String getHeader(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames()).stream()
        .map(headerName -> headerName + ":" + Collections.list(request.getHeaders(headerName)) )
        .collect(Collectors.joining(", "));
  }
}
