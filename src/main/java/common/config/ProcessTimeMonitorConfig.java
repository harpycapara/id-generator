package common.config;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "process-time-monitor")
@Data
@ToString
@Slf4j
public class ProcessTimeMonitorConfig {

    private long baseWarningMs;

    @PostConstruct
    void init() {
        log.info("ProcessTimeMonitorConfig: {}", this);
    }

}
