package com.spark.steaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.Map;


public class SparkSteamTest {

    public static void main(String[] args) {
        SparkSteamTest.streamTest();
    }

    static void streamTest() {
        SparkConf conf = new SparkConf().setMaster("master").setAppName("local[*]");
//        StreamingContext ssc = new StreamingContext(conf, Seconds.apply(10));
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));
        JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181","spark-streaming-consumer-group", Map.of("spark-topic" , 5));
        kafkaStream.print();
        ssc.start();
        ssc.awaitTermination();

//        JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(1000));
//        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
//        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")));
//        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
//        JavaPairDStream<String, Integer> wordcounts = pairs.reduceByKey((i1, i2) -> i1 + i2);
//        wordcounts.print();
//        jssc.start();
//        jssc.awaitTermination();
    }

}
