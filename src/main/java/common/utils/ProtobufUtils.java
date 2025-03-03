package common.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import com.google.rpc.ErrorInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ProtobufUtils {
  private static final JsonFormat.TypeRegistry TYPE_REGISTRY =
      JsonFormat.TypeRegistry.newBuilder().add(ErrorInfo.getDescriptor()).build();

  private static final Printer PRINTER =
      JsonFormat.printer()
          .omittingInsignificantWhitespace()
          .includingDefaultValueFields()
          .usingTypeRegistry(TYPE_REGISTRY);

  private ProtobufUtils() {
  }

  public static String print(MessageOrBuilder message) {
    try {
      return PRINTER.print(message);
    } catch (Exception ex) {
      LogUtils.error("Reason: ", ex);
    }
    return null;
  }

  public static Map<String, Object> protoToMap(MessageOrBuilder proto) {
    final Map<Descriptors.FieldDescriptor, Object> allFields = proto.getAllFields();
    Map<String, Object> map = new LinkedHashMap<>();
    for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : allFields.entrySet()) {
      final Descriptors.FieldDescriptor fieldDescriptor = entry.getKey();
      final Object requestVal = entry.getValue();
      final Object mapVal = convertVal(proto, fieldDescriptor, requestVal);
      if (mapVal != null) {
        final String fieldName = fieldDescriptor.getName();
        map.put(fieldName, mapVal);
      }
    }
    return map;
  }


  private static Object convertVal(@NotNull MessageOrBuilder proto, @NotNull Descriptors.FieldDescriptor fieldDescriptor,
                                   @Nullable Object protoVal) {
    Object result = null;
    if (protoVal != null) {
      if (fieldDescriptor.isRepeated()) {
        if (proto.getRepeatedFieldCount(fieldDescriptor) > 0) {
          final List originals = (List) protoVal;
          final List copies = new ArrayList(originals.size());
          for (Object original : originals) {
            copies.add(convertAtomicVal(fieldDescriptor, original));
          }
          result = copies;
        }
      } else {
        result = convertAtomicVal(fieldDescriptor, protoVal);
      }
    }
    return result;
  }

  private static Object convertAtomicVal(@NotNull Descriptors.FieldDescriptor fieldDescriptor,
                                         @Nullable Object protoVal) {
    Object result = null;
    if (protoVal != null) {
      switch (fieldDescriptor.getJavaType()) {
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case BOOLEAN:
        case STRING:
          result = protoVal;
          break;
        case BYTE_STRING:
        case ENUM:
          result = protoVal.toString();
          break;
        case MESSAGE:
          result = protoToMap((Message) protoVal);
          break;
      }
    }
    return result;
  }

}
