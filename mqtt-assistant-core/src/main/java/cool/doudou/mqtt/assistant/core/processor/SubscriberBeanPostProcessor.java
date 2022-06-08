package cool.doudou.mqtt.assistant.core.processor;

import cool.doudou.mqtt.assistant.annotation.MqttSubscriber;
import cool.doudou.mqtt.assistant.core.ConcurrentMapFactory;
import cool.doudou.mqtt.assistant.core.entity.SubscribeMethod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * SubscriberBeanPostProcessor
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Slf4j
@AllArgsConstructor
public class SubscriberBeanPostProcessor implements BeanPostProcessor {
    private String[] topics;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter((method) -> method.isAnnotationPresent(MqttSubscriber.class))
                .forEach((method) -> {
                    MqttSubscriber mqttClient = method.getAnnotation(MqttSubscriber.class);
                    initMethod(mqttClient.topics(), topics, bean, method);
                });
        return bean;
    }

    /**
     * 初始化 Method
     *
     * @param topics    需回调的消息主题
     * @param subTopics 已订阅的消息主题
     * @param bean      方法类
     * @param method    方法
     */
    private void initMethod(String[] topics, String[] subTopics, Object bean, Method method) {
        if (topics == null || topics.length <= 0) {
            topics = subTopics;
        }

        Arrays.stream(topics).forEach((topic) -> ConcurrentMapFactory.addMethod(topic, SubscribeMethod.builder().bean(bean).method(method).build()));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
