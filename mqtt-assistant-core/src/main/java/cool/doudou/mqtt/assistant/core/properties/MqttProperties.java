package cool.doudou.mqtt.assistant.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * MqttProperties
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String serverUri = "tcp://127.0.0.1:1883";
    private String username = "admin";
    private String password = "1234.abcd";
    private String clientId = UUID.randomUUID().toString();
    private String[] topics = {"default"};
    private Integer qos = 0;
}
