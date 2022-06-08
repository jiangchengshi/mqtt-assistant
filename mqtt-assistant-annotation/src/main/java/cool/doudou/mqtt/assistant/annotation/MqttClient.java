package cool.doudou.mqtt.assistant.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MqttClient
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MqttClient {
    /**
     * @return 回调消息主题
     */
    String[] topics();
}
