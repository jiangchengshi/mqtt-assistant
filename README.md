### 引入依赖

```kotlin
implementation("cool.doudou:mqtt-assistant:latest")
```

> Latest
> Version: [![Maven Central](https://img.shields.io/badge/Maven-v1.0.0-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:mqtt-assistant-*)


### MQTT配置

> 依赖spring自动注入MqttPahoClientFactory，配置属性如下：

```yaml
mqtt:
  server-uri: tcp://127.0.0.1:1883
  username: admin
  password: 1234.abcd
  client-id: test
  topics:
    - "celery"
```

### 使用方式

> 消息订阅：两种方式如下

```
1. 类添加注解 @MqttClient，实现 MqttSubscribeCallback 接口，重写 messageArrived 方法
2. 方法添加注解 @MqttSubscriber(topic = "celery")，topic为订阅主题

注意：两种方式任意一种都可以，若两种方式都配置，则第一种优先
```

```java

@Component
@MqttClient(topics = {"celery"})
public class MqttComponent implements MqttSubscribeCallback {
    /**
     * 重写 messageArrived 方法
     * @param topic 订阅主题
     * @param payload 数据载荷
     */
    @Override
    public void messageArrived(String topic, byte[] payload) {
        System.out.println("messageArrived: topic[" + topic + "] => " + Arrays.toString(bytes));
    }
}

@Component
public class MqttComponent {
    /**
     * 自定义方法
     * @param topic 订阅主题
     * @param payload 数据载荷
     */
    @MqttSubscriber(topic = {"celery"})
    public void receive(String topic, byte[] payload) {
        System.out.println("receive: topic[" + topic + "] => " + Arrays.toString(bytes));
    }
}
```

> 消息发送：依赖注入 MqHelper，调用 send(String topic, String payload) 方法

```java

@AllArgsConstructor
@Component
public class MqttComponent {
    private MqttHelper mqttHelper;

    /**
     * 消息发送
     * @param topic 发送主题
     * @param payload 数据载荷
     */
    public void test(String topic, byte[] payload) {
        boolean sendFlag = mqttHelper.send("celery-send", payload);
        System.out.println("send: " + sendFlag);
    }
}
```

### 鼓励一下，喝杯咖啡

![鼓励一下，喝杯咖啡](https://user-images.githubusercontent.com/21210629/172556529-544b2581-ea34-4530-932b-148198b1b265.jpg)
