package common.base;

import common.annotations.MappingAnnonation;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.googlecode.jmapper.api.MappedClass;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.jmapper.api.JMapperAPI.*;
import static common.utils.Strings.upperCamelCase;
import static io.micrometer.core.instrument.config.NamingConvention.upperCamelCase;

@Component
public class MappingFactory {

  /**
   * Key: request class
   */
  private Map<Class, JMapper> mappers = new ConcurrentHashMap<>();

  /**
   * Key: arg class
   * Value: request class
   */
  private Map<Class, Class> mapFroms = new ConcurrentHashMap<>();

  public MappingFactory() {
    Reflections reflections = new Reflections("bank_binding.domain.args");
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MappingAnnonation.class);
    JMapperAPI api = new JMapperAPI();
    for (Class<?> argClass : annotated) {
      MappedClass mappedClass = getMappedClass(argClass);
      api.add(mappedClass);
    }

    for (Class<?> argClass : annotated) {
      MappingAnnonation annotation = argClass.getAnnotation(MappingAnnonation.class);
      JMapper mapper = new JMapper(argClass, annotation.from(), api);
      mappers.put(annotation.from(), mapper);
      mapFroms.put(argClass, annotation.from());
    }
  }

  public Class getMapFromType(Class argClass) {
    return mapFroms.get(argClass);
  }

  public Object mapFromRequest(Object request) {
    if (mappers.containsKey(request.getClass())) {
      return mappers.get(request.getClass()).getDestination(request);
    }
    return null;
  }

  private static MappedClass getMappedClass(Class<?> clazz) {
    MappedClass mapped = mappedClass(clazz);
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      boolean isTransient = Modifier.isTransient(field.getModifiers());
      boolean isStatic = Modifier.isStatic(field.getModifiers());
      if (isTransient || isStatic) {
        continue;
      }
      // Add "_" to match protobuf field name
      mapped.add(
          attribute(field.getName())
              .targetAttributes(
                  targetAttribute(field.getName() + "_",
                                  "get" + upperCamelCase(field.getName()),
                                  "set" + upperCamelCase(field.getName()))
              )
      );
    }
    return mapped;
  }

}
