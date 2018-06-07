package com.kafka.test;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkaProducer {
    private final static String TOPIC = "spark-topic";
    private final static String BOOTSTRAP_SERVERS =
            "192.168.0.101:9092,localhost:9093,localhost:9094";

    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }



    static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<Long, String> record =
                            new ProducerRecord<>(TOPIC,index,
                                "Hello Mom " + index);
                RecordMetadata metadata = producer.send(record).get();
                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("sent record(key=%s value=%s) " +
                                "meta(partition=%d, offset=%d) time=%d\n",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);
            }
        } finally {
            producer.flush();
            producer.close();
        }
    }

    static void runAsynProducer(final int sendMessageCount) throws Exception {
        final Producer producer = createProducer();
        long time = System.currentTimeMillis();

        final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);

        try {
            for(long index = time;index< time + sendMessageCount; index++)
            {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(TOPIC,index,
                                "Hello Mom " + index);
                producer.send(record,(recordMetadata, e) -> {
                    long elapsedTime = System.currentTimeMillis() - time;
                    if (recordMetadata != null) {
                        System.out.printf("sent record(key=%s value=%s) " +
                                        "meta(partition=%d, offset=%d) time=%d\n",
                                record.key(), record.value(), recordMetadata.partition(),
                                recordMetadata.offset(), elapsedTime);
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });
            }
        }finally {
            producer.flush();
            producer.close();
        }
    }

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            runAsynProducer(50);
        } else {
            runProducer(Integer.parseInt(args[0]));
        }
    }

}
