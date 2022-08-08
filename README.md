# 欢迎使用 Mqtt-Assistant

[![Maven](https://img.shields.io/badge/Maven-v2.0.1-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:mqtt-assistant-*)
[![License](https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
![SpringBoot](https://img.shields.io/badge/SpringBoot-v2.7.2-blue)

## 简介

Mqtt助手 - 简化配置，注解带飞！

## 特点

> 基于spring-integration-mqtt包，没有改变任何框架结构，只为简化开发；配置灵活，简单注解，即可进行协议通信

## 使用指引

### 引入依赖

```kotlin
implementation("cool.doudou:mqtt-assistant-boot-starter:latest")
```

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

> 消息发送：依赖注入 MqHelper

```java

@AllArgsConstructor
@Component
public class MqttComponent {
    private MqttHelper mqttHelper;

    public void test() {
        // 发送 byte数组
        byte[] byteArr = new byte[]{0x01, 0x02};
        boolean sendFlag1 = mqttHelper.send("celery", byteArr);
        System.out.println("send: " + sendFlag1);

        // 发送 字符串
        boolean sendFlag2 = mqttHelper.send("celery", "您好");
        System.out.println("send: " + sendFlag2);
    }
}
```

## 版权

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## 鼓励一下，喝杯咖啡

> 欢迎提出宝贵意见，不断完善 Mqtt-Assistant

![鼓励一下，喝杯咖啡](https://user-images.githubusercontent.com/21210629/172556529-544b2581-ea34-4530-932b-148198b1b265.jpg)
