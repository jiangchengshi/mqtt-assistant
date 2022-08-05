package cool.doudou.mqtt.assistant.core.handler;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * MqttGatewayHandler
 *
 * @author jiangcs
 * @since 2022/3/4
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGatewayHandler {
    /**
     * 发送
     *
     * @param topic   主题
     * @param payload 数据载荷
     * @throws Exception 异常
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, byte[] payload) throws Exception;

    /**
     * 发送
     *
     * @param topic   主题
     * @param qos     0 表示的是订阅者没收到消息不会再次发送，消息会丢失。<br> 1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。<br> 2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param payload 数据载荷
     * @throws Exception 异常
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, byte[] payload) throws Exception;
}
