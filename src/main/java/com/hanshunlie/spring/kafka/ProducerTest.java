package com.hanshunlie.spring.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerTest {

    public static String brokers = "1.15.40.211:9091";

    public static Properties inifConf(){
        Properties conf = new Properties();
        conf.setProperty(ProducerConfig.ACKS_CONFIG,"0");
        conf.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        conf.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
        conf.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());

        conf.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,"16384"); //16k 要调整的,分析我们msg的大小，尽量触发批次发送，减少内存碎片，和系统调用的复杂度
        conf.setProperty(ProducerConfig.LINGER_MS_CONFIG,"0");  //


        conf.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"1048576");
        //message.max.bytes


        conf.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG,"33554432");//32M
        conf.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG,"60000"); //60秒

        conf.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");

        conf.setProperty(ProducerConfig.SEND_BUFFER_CONFIG,"32768");  //32K   -1
        conf.setProperty(ProducerConfig.RECEIVE_BUFFER_CONFIG,"32768"); //32k  -1
        return conf;




    }

    public static void main123(String[] args) {
        Properties conf = inifConf();
        KafkaProducer kafkaProducer = new KafkaProducer<>(conf);

        ProducerRecord<String, String> msg = new ProducerRecord<>("ooxx", "hello", "hi");

        Future future = kafkaProducer.send(msg);

        Future send = kafkaProducer.send(msg, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {

            }
        });

        while(true){

        }

    }
}
