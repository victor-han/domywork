package com.hanshunlie.spring.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Future;

public class kafkaTest {


    /**
     * create topic
     * <p>
     * kafka-topics.sh --zookeeper 1.15.40.211:2181/kafka --create --topic hsl-items --partitions 2 --replication-factor 1
     */
    String topic = "hsl-items";


    @Test
    public void producer() {

//        String topic = "hsl-items";

        Properties p = new Properties();
        p.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "1.15.40.211:9091");
        //kafka 持久化消息的MQ，数据形式是byte[],kafka不会对数据进行干预，所以双方需要约定编解码
        //因为kafka是一个app, 可以是用零拷贝，sendfile，系统调用实现快速数据消费
        //零拷贝：在数据传输的时候，减少拷贝的次数。todo 什么是sendifle

        p.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        p.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(p);

        //现在的producer就是一个提供者，面相的其实是broker， 虽然在使用的时候，我们希望把数据打入topic

        /**
         * hsl-items
         * partitions 2
         * 三种商品，每种商品有线性的3个id
         * 相同的商品最好取到一个分区里
         * */

        try {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, "item" + j, "value" + i);

                    Future<RecordMetadata> send =
                            producer.send(record);
                    RecordMetadata rm = send.get();
                    int partition = rm.partition();
                    long offset = rm.offset();
                    System.err.println("key : " + record.key() + "  ,val : " + record.value() + "  ,partition" + partition + "  ,offset : " + offset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void consumer() {
        //基础配置
        Properties p = new Properties();
        p.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "1.15.40.211:9091");
        p.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        p.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        //消费的细节
        //一个partition只能被一个组里面的一个consumer消费
        p.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "OOXX01");
        //KAFKA is mq, also is storage
        //从哪里消费
        p.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        //开启自动提交,是异步的，间隔几秒。容易造成丢数据&&重复数据
        p.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        // 提交时间间隔
//        p.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG , "");

        //通过poll拉取数据，弹性的，按需，拉取多少配置
//        p.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "");


        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(p);

        //kafka的consumer会动态负载均衡
        consumer.subscribe(Arrays.asList("hsl-items"), new ConsumerRebalanceListener() {
            //我消失了哪些分区
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("-------onPartitionsRevoked");
                Iterator<TopicPartition> iterator = partitions.iterator();
                while (iterator.hasNext()) {
                    System.out.println((iterator.next()));
                }
            }

            //我得到了哪些分区
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("-------onPartitionsAssigned");
                Iterator<TopicPartition> iterator = partitions.iterator();
                while (iterator.hasNext()) {
                    System.out.println((iterator.next()));
                }
            }
        });


        /**
         *  以下代码是你在蔚来开发的时候，通过自定义的时间的方式，自定义消费数据位置
         *  核心是seek
         *
         *  1、通过时间换算出offset，在通过seek在自定义便宜
         *  2、如果自己维护offset持久化，通过seek完成
         */

        Map<TopicPartition, Long> tts = new HashMap<>();

        //通过consumer
        Set<TopicPartition> assignment = consumer.assignment();

        while (assignment.size() == 0) {
            consumer.poll(Duration.ofMillis(100));
            assignment = consumer.assignment();
        }

        //自己填充一个hashmap，为每个分区设置对应的时间戳
        for (TopicPartition partition : assignment) {
            tts.put(partition, 0l);
        }
        //通过consumer的api，取回time index的数据.根据时间拿到index的数据
        Map<TopicPartition, OffsetAndTimestamp> offtime = consumer.offsetsForTimes(tts);
        //根据index，获取偏移量
        for (TopicPartition partition : assignment) {
            OffsetAndTimestamp offsetAndTimestamp = offtime.get(partition);
            long offset = offsetAndTimestamp.offset();

            consumer.seek(partition, offset);
        }


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
            //微批的感觉
            ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(0));//0~N条
            if (!records.isEmpty()) {
                System.out.println("-----------------------" + records.count() + "-------------------");

                //按分区消费
                Set<TopicPartition> partitions = records.partitions();
                for (TopicPartition partition : partitions) {
                    List<ConsumerRecord<Object, Object>> pRecords = records.records(partition);
                    Iterator<ConsumerRecord<Object, Object>> iterator = pRecords.iterator();
                    while (iterator.hasNext()) {
                        ConsumerRecord<Object, Object> next = iterator.next();
                        //TODO 消费数据
                        int par = next.partition();
                        long offset = next.offset();

                        TopicPartition sp = new TopicPartition(topic, par);
                        OffsetAndMetadata om = new OffsetAndMetadata(offset);
                        HashMap<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
                        map.put(sp, om);
                        //按照没条记录粒度提交
                        consumer.commitSync(map);

                    }


                    long poff = pRecords.get(pRecords.size() - 1).offset();

                    OffsetAndMetadata om = new OffsetAndMetadata(poff);
                    HashMap<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
                    map.put(partition, om);
                    //按照分区粒度
                    consumer.commitSync(map);

                }

                //按照批次
                consumer.commitSync();

                //按批次里面的顺序消费
//                Iterator<ConsumerRecord<Object, Object>> iter = records.iterator();
//                while (iter.hasNext()) {
//                    ConsumerRecord<Object, Object> record = iter.next();
//                    long offset = record.offset();
//                    int partition = record.partition();
//
//                    System.out.println("key :" + record.key() + "   val :" + record.value());
//                }
            }

        }

    }
}
