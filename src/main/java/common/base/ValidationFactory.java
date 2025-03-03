package common.base;

import common.annotations.ValidationAnnotation;
import io.vavr.control.Either;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ValidationFactory {
  private Map<Class, Validation> validations = new ConcurrentHashMap<>(); // mapping request - validation
  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private Validator validator;

  @PostConstruct
  @SneakyThrows
  private void init() {
    Reflections reflections = new Reflections("bank_binding.application.validation");
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ValidationAnnotation.class);
    String notValidations = annotated.stream()
        .filter(clazz -> !Validation.class.isAssignableFrom(clazz))
        .map(Class::getName)
        .reduce(new StringJoiner(", "), StringJoiner::add, StringJoiner::merge).toString();
    if (!notValidations.isEmpty()) {
      throw new IllegalClassFormatException("These classes are annotated with @ValidationAnnotation but not implement" +
                                                " Validation interface: " + notValidations);
    }
    for (Class<?> clazz : annotated) {
      Class<? extends Validation<?>> validationClass = (Class<? extends Validation<?>>) clazz;
      ValidationAnnotation annotation = validationClass.getAnnotation(ValidationAnnotation.class);
      Validation<?> validation = applicationContext.getBean(validationClass);
      this.validations.put(annotation.request(), validation);
    }
  }

  public Validation getValidationForRequest(Object request) {
    if (validations.containsKey(request.getClass())) {
      return validations.get(request.getClass());
    }
    return args -> {
      Set<ConstraintViolation<Object>> violations = validator.validate(args);
      if (!violations.isEmpty()) {
        return Either.left(new Result.Error(
            new IllegalArgumentException(
                violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce(new StringJoiner(", "),
                            StringJoiner::add,
                            StringJoiner::merge).toString()
            )
        ));
      }
      return Either.right(true);
    };
  }

}
