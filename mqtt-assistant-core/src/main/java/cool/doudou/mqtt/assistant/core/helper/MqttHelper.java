package cool.doudou.mqtt.assistant.core.helper;

import cool.doudou.mqtt.assistant.core.handler.MqttGatewayHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

/**
 * MqttHelper
 *
 * @author jiangcs
 * @since 2022/3/4
 */
@Slf4j
public class MqttHelper {
    private MqttGatewayHandler mqttGatewayHandler;

    /**
     * 发送
     *
     * @param topic   主题
     * @param payload 数据载荷
     * @return true-成功；false-失败
     */
    public boolean send(String topic, String payload) {
        return send(topic, payload.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 发送
     *
     * @param topic   主题
     * @param payload 数据载荷
     * @return true-成功；false-失败
     */
    public boolean send(String topic, byte[] payload) {
        try {
            mqttGatewayHandler.send(topic, payload);
            return true;
        } catch (Exception e) {
            log.error("send topic[{}],payload[{}] exception: ", topic, payload, e);
        }
        return false;
    }

    /**
     * 发送
     *
     * @param topic   主题
     * @param pos     QOS
     * @param payload 数据载荷
     * @return true-成功；false-失败
     */
    public boolean send(String topic, int pos, String payload) {
        return send(topic, pos, payload.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 发送
     *
     * @param topic   主题
     * @param pos     QOS
     * @param payload 数据载荷
     * @return true-成功；false-失败
     */
    public boolean send(String topic, int pos, byte[] payload) {
        try {
            mqttGatewayHandler.send(topic, pos, payload);
            return true;
        } catch (Exception e) {
            log.error("send topic[{}],pos[{}],payload[{}] exception: ", topic, pos, payload, e);
        }
        return false;
    }

    @Autowired(required = false)
    public void setMqttGatewayHandler(MqttGatewayHandler mqttGatewayHandler) {
        this.mqttGatewayHandler = mqttGatewayHandler;
    }
}
