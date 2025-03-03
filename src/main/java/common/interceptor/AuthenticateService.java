package common.interceptor;

/**
 * @author tuannlh
 */
public interface AuthenticateService {
    void authenticate(String clientId, String clientKey, String api);
}
