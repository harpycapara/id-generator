package common.interceptor;


import common.config.ApiClientKeyConfig;
import common.utils.HandlerValidator;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticateService {
    private final ApiClientKeyConfig config;
    private final List<String> EMPTY_LIST = new ArrayList<>();

    @Override
    public void authenticate(String clientId, String clientKey, String api) {
        val clientKeyMap = config.getClientKey();
        val apiClientMap = config.getApiClient();

        HandlerValidator.validateAuthenticationEmpty("client-id", clientId);
        HandlerValidator.validateAuthenticationEmpty("client-key", clientKey);
        HandlerValidator.validateAuthenticationCondition(!clientKey.equals(clientKeyMap.getOrDefault(clientId, "")),
                                        "client-key or client-id id is invalid");

        val clientIdList = apiClientMap.getOrDefault(api, EMPTY_LIST);
        HandlerValidator.validateAuthorizationCondition(!clientIdList.contains(clientId),
                                       "client-id is not authorized to call this api");
    }
}
