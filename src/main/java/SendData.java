import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class SendData {

    static String url = "jdbc:mysql://localhost:3306/test?";
    static String user = "root";
    static String passwd = "123456";

    static String kafkaTopc = "lakesoul_read_kafka";

    static String kafkaOrmysql;

    static String kafkahost = "localhost:3306";

    static int rowNums = 20000000;
    private static Properties properties;
    public static void main(String[] args) throws SQLException {
        if (args.length==5){
            kafkaOrmysql = args[0];
            url = args[1];
            user = args[2];
            passwd = args[3];
            rowNums = Integer.parseInt(args[4]);
            List<Operation> operationList = Operation.generateOperations(rowNums);
            sendMysql(operationList);
        }
        else if (args.length==4){
            kafkaOrmysql = args[0];
            kafkaTopc = args[1];
            kafkahost = args[2];
            rowNums = Integer.parseInt(args[3]);
            List<Operation> operationList = Operation.generateOperations(rowNums);
            properties = getConf();
            sendKafka(kafkaTopc, operationList);
        }else {
            throw new RuntimeException("The parameter is incorrect");
        }

    }

    public static void sendKafka(String topicName, List<Operation> ops){
        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<>(properties);

            for (Operation o : ops) {
                String id = o.getClient_ip();
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, id, convertOperationToString(o));
                producer.send(record);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // 一定要关闭资源，否则消息发送不成功
            try {
                if (producer != null) {
                    producer.close();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public static void sendMysql(List<Operation> ops) throws SQLException {

        Connection connection = DriverManager.getConnection(url, user, passwd);
        //String sql = String.format("create table if not EXISTS lakesoul_test_mysql_table(`client_ip`)")
        String sql = "CREATE TABLE IF NOT EXISTS lakesoul_test_mysql_table " +
                "(`client_ip` VARCHAR(100)," +
                "`domain` VARCHAR(100)," +
                "`time` VARCHAR(100)," +
                "`target_ip` VARCHAR(100)," +
                "`rcode` VARCHAR(255)," +
                "`query_type` VARCHAR(255)," +
                "`authority_record` VARCHAR(255)," +
                "`add_msg` VARCHAR(255), " +
                "`dns_ip` VARCHAR(255)," +
                "PRIMARY KEY (`client_ip`, `domain`, `time`, `target_ip`));";

        Statement statement = connection.createStatement();
        statement.execute(sql);

        for (Operation op : ops) {
            String insertSql = String.format("insert into lakesoul_test_mysql_table values('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                    op.getClient_ip(),op.getDomain(),op.getTime(),op.getTarget_ip(),op.getRcode(),
                    op.getQuery_type(),op.getAuhority_record(),op.getAdd_msg(),op.getDns_ip());
            statement.execute(insertSql);
        }
        statement.close();
        connection.close();
    }
    private static Properties getConf() {

        Properties properties = new Properties();
        // ProducerConfig 类定义了生产者相关的配置
        // 指定kafka 连接的集群
//        properties.put("bootstrap.servers", "localhost:9093");
        properties.put("bootstrap.servers", kafkahost);
        // ack应答级别
        properties.put("acks", "all");
        // 重试次数
        properties.put("retries", 0);
        // 单次发送批次的大小
        properties.put("batch.size", 163840);
//        properties.put("batch.size", 1);
        // 批次大小不足的情况等待发送时间
        properties.put("linger.ms", 1);
        // RecordAccumulator 缓冲区大小
        properties.put("buffer.memory", 33554432);
//        properties.put("buffer.memory", 2000);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return properties;
    }

    private static String convertOperationToString(Operation operation) {
        // Implement the logic to convert Operation object to a string representation
        // For example, concatenate fields with some delimiter
        return operation.getClient_ip() + "," +
                operation.getDomain() + "," +
                operation.getTime() + "," +
                operation.getTarget_ip() + "," +
                operation.getRcode() + "," +
                operation.getQuery_type() +"," +
                operation.getAuhority_record() +"," +
                operation.getAdd_msg() + "," +
                operation.getDns_ip();
    }
}
