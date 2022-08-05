package cool.doudou.mqtt.assistant.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MqttSubscriber
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MqttSubscriber {
    /**
     * @return 回调消息主题
     */
    String[] topics() default {};
}
