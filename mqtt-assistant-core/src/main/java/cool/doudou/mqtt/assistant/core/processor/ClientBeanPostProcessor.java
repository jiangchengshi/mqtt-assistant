package cool.doudou.mqtt.assistant.core.processor;

import cool.doudou.mqtt.assistant.annotation.MqttClient;
import cool.doudou.mqtt.assistant.core.ConcurrentMapFactory;
import cool.doudou.mqtt.assistant.core.callback.MqttSubscribeCallback;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

/**
 * ClientBeanPostProcessor
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Slf4j
@AllArgsConstructor
public class ClientBeanPostProcessor implements BeanPostProcessor {
    private String[] topics;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        boolean annotationPresent = bean.getClass().isAnnotationPresent(MqttClient.class);
        if (annotationPresent) {
            if (bean instanceof MqttSubscribeCallback) {
                MqttClient mqttClient = bean.getClass().getDeclaredAnnotation(MqttClient.class);
                initClient(mqttClient.topics(), topics, (MqttSubscribeCallback) bean);
            } else {
                log.warn("@MqttClient: bean[{}] should implements MqttSubscribeCallback", bean);
            }
        }
        return bean;
    }

    /**
     * 初始化Client
     *
     * @param topics    需回调的消息主题
     * @param subTopics 已订阅的消息主题
     * @param callback  回调方法
     */
    private void initClient(String[] topics, String[] subTopics, MqttSubscribeCallback callback) {
        if (topics == null || topics.length <= 0) {
            topics = subTopics;
        }

        Arrays.stream(topics).forEach((topic) -> ConcurrentMapFactory.addCallback(topic, callback));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
