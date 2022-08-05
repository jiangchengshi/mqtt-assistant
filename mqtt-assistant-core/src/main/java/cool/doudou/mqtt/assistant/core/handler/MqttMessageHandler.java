package cool.doudou.mqtt.assistant.core.handler;

import cool.doudou.mqtt.assistant.core.ConcurrentMapFactory;
import cool.doudou.mqtt.assistant.core.callback.MqttSubscribeCallback;
import cool.doudou.mqtt.assistant.core.entity.SubscribeMethod;
import cool.doudou.mqtt.assistant.core.properties.MqttProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.lang.reflect.Method;

/**
 * MqttMessageHandler
 *
 * @author jiangcs
 * @since 2022/3/4
 */
@Slf4j
@AllArgsConstructor
public class MqttMessageHandler {
    private MqttProperties mqttProperties;

    /**
     * 消息接收
     *
     * @param mqttPahoClientFactory PahoClient工厂
     * @param mqttInboundChannel    消息通道Inbound
     * @param mqttErrorChannel      消息通道Error
     * @return MessageProducer
     */
    @Bean
    public MessageProducer mqttInbound(MqttPahoClientFactory mqttPahoClientFactory, MessageChannel mqttInboundChannel,
                                       MessageChannel mqttErrorChannel) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getClientId() + "_in",
                mqttPahoClientFactory,
                mqttProperties.getTopics()
        );
        adapter.setCompletionTimeout(5000);
        // 使用默认消息转换器：处理返回byte
        DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
        defaultPahoMessageConverter.setPayloadAsBytes(true);
        adapter.setConverter(defaultPahoMessageConverter);
        adapter.setOutputChannel(mqttInboundChannel);
        adapter.setErrorChannel(mqttErrorChannel);
        adapter.setQos(mqttProperties.getQos());
        return adapter;
    }

    /**
     * 通过通道获取数据
     * <p>
     * ServiceActivator: 指定接收消息的管道为 mqttInboundChannel，投递到 mqttInboundChannel 管道中的消息会被该方法接收并执行
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = null;
            MessageHeaders headers = message.getHeaders();
            if (!headers.isEmpty()) {
                topic = String.valueOf(headers.get("mqtt_receivedTopic"));
            }
            if (topic == null) {
                log.error("handler: topic is null");
            } else {
                // 数据载荷
                Object payload = message.getPayload();

                // Handler
                MqttSubscribeCallback callback = ConcurrentMapFactory.getCallback(topic);
                if (callback != null) {
                    callback.messageArrived(topic, payload);
                    return;
                }

                // Method
                SubscribeMethod subscribeMethod = ConcurrentMapFactory.getMethod(topic);
                if (subscribeMethod != null) {
                    Object bean = subscribeMethod.getBean();
                    Method method = subscribeMethod.getMethod();
                    try {
                        method.setAccessible(true);
                        method.invoke(bean, payload);
                    } catch (Exception e) {
                        log.error("bean[{}].method[{}] invoke exception: ", bean, method.getName(), e);
                    }
                    return;
                }

                log.warn("topic[{}]: No handler or method found", topic);
            }
        };
    }

    /**
     * 通过通道获取数据
     * <p>
     * ServiceActivator: 指定接收消息的管道为 mqttErrorChannel，投递到 mqttErrorChannel 管道中的消息会被该方法接收并执行
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttErrorChannel")
    public MessageHandler errorHandler() {
        return message -> {
            log.error("errorHandler: {}", message);
        };
    }

    /**
     * 通过通道获取数据
     * <p>
     * ServiceActivator: 指定接收消息的管道为 mqttOutboundChannel，投递到 mqttOutboundChannel 管道中的消息会被该方法接收并执行
     *
     * @param mqttPahoClientFactory PahoClient工厂
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttProperties.getClientId() + "_out",
                mqttPahoClientFactory
        );
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttProperties.getTopics()[0]);
        return messageHandler;
    }
}
