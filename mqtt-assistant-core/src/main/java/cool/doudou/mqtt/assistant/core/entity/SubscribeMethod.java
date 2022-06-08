package cool.doudou.mqtt.assistant.core.entity;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * SubscribeMethod
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Builder
@Data
public class SubscribeMethod {
    private Object bean;
    private Method method;
}
