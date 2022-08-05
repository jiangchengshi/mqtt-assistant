package cool.doudou.mqtt.assistant.core.config;

import cool.doudou.mqtt.assistant.core.processor.ClientBeanPostProcessor;
import cool.doudou.mqtt.assistant.core.processor.SubscriberBeanPostProcessor;
import cool.doudou.mqtt.assistant.core.properties.MqttProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessageChannel;

/**
 * MqttConfig
 * <p>
 * Important:
 * The IntegrationComponentScan annotation is required along with
 * org.springframework.context.annotation.Configuration to scan interfaces annotated with MessagingGateway,
 * because the standard org.springframework.context.annotation.ComponentScan ignores interfaces.
 *
 * @author jiangcs
 * @since 2022/3/3
 */
@Slf4j
@AllArgsConstructor
@IntegrationComponentScan("cool.doudou.mqtt.assistant.core.handler")
public class MqttConfig {
    private MqttProperties mqttProperties;

    /**
     * @return PahoClient工厂
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setServerURIs(new String[]{mqttProperties.getServerUri()});
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        factory.setConnectionOptions(options);

        log.info("mqtt: serverUri[{}],clientId[{}]", mqttProperties.getServerUri(), mqttProperties.getClientId());

        return factory;
    }

    /**
     * @return 消息通道 inbound
     */
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * @return 消息通道 error
     */
    @Bean
    public MessageChannel mqttErrorChannel() {
        return new DirectChannel();
    }

    /**
     * @return 消息通道 outbound
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * @return 注解 @MqClient 处理器
     */
    @Bean
    public ClientBeanPostProcessor clientBeanPostProcessor() {
        return new ClientBeanPostProcessor(mqttProperties.getTopics());
    }

    /**
     * @return 注解 @MqSubscriber 处理器
     */
    @Bean
    public SubscriberBeanPostProcessor subscriberBeanPostProcessor() {
        return new SubscriberBeanPostProcessor(mqttProperties.getTopics());
    }
}
