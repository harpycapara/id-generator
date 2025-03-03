package common.config;

import common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "validator")
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidatorConfig {

  private List<String> constraintPaths;

  @Bean
  public Validator validator() {
    log.info("Constraint paths: {}", GsonUtils.toJson(constraintPaths));
    HibernateValidatorConfiguration config = Validation.byProvider(HibernateValidator.class).configure();

    if (constraintPaths != null) {
      for (String path : constraintPaths) {
        config.addMapping(this.getClass().getClassLoader().getResourceAsStream(path));
      }
    }

    try (ValidatorFactory validatorFactory = config.buildValidatorFactory()) {
      return validatorFactory.getValidator();
    }
  }

}
