package cool.doudou.mqtt.assistant.boot.starter.auto.configuration;

import cool.doudou.mqtt.assistant.core.config.MqttConfig;
import cool.doudou.mqtt.assistant.core.handler.MqttMessageHandler;
import cool.doudou.mqtt.assistant.core.helper.MqttHelper;
import cool.doudou.mqtt.assistant.core.properties.MqttProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * MqttAutoConfiguration
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@EnableConfigurationProperties({MqttProperties.class})
@Import({MqttConfig.class, MqttMessageHandler.class})
@AutoConfiguration
public class MqttAutoConfiguration {
    @ConditionalOnMissingBean(MqttHelper.class)
    @Bean
    public MqttHelper mqttHelper() {
        return new MqttHelper();
    }
}
