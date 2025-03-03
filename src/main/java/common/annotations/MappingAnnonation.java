package common.annotations;

import com.google.protobuf.MessageOrBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappingAnnonation {

  Class<? extends MessageOrBuilder> from();

}
