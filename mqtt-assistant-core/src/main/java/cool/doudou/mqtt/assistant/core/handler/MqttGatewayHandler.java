package cool.doudou.mqtt.assistant.core.handler;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

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
     * @param payload 数据载荷
     * @throws Exception 异常
     */
    void send(byte[] payload) throws Exception;

    /**
     * 发送
     *
     * @param topic   主题
     * @param payload 数据载荷
     * @throws Exception 异常
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, byte[] payload) throws Exception;
}
