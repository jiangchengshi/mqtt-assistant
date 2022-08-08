package cool.doudou.mqtt.assistant.core.callback;

/**
 * MqttSubscribeCallback
 *
 * @author jiangcs
 * @since 2022/3/3
 */
public interface MqttSubscribeCallback {
    /**
     * 处理消息
     *
     * @param topic   订阅主题
     * @param payload 负载数据
     */
    void messageArrived(String topic, byte[] payload);
}
