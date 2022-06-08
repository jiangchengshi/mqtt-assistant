package cool.doudou.mqtt.assistant.core;

import cool.doudou.mqtt.assistant.core.callback.MqttSubscribeCallback;
import cool.doudou.mqtt.assistant.core.entity.SubscribeMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentMapFactory
 *
 * @author jiangcs
 * @since 2022/3/3
 */
public class ConcurrentMapFactory {
    private static final Map<String, MqttSubscribeCallback> CALLBACK_MAP = new ConcurrentHashMap<>();
    private static final Map<String, SubscribeMethod> METHOD_MAP = new ConcurrentHashMap<>();

    public static MqttSubscribeCallback getCallback(String topic) {
        return CALLBACK_MAP.get(topic);
    }

    public static void addCallback(String topic, MqttSubscribeCallback callback) {
        CALLBACK_MAP.put(topic, callback);
    }

    public static SubscribeMethod getMethod(String topic) {
        return METHOD_MAP.get(topic);
    }

    public static void addMethod(String topic, SubscribeMethod method) {
        METHOD_MAP.put(topic, method);
    }
}
