import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Operation {

    private String client_ip;

    private String domain;

    private String time;

    private String target_ip;

    private String rcode;

    private String query_type;

    private String auhority_record;

    private String add_msg;

    private String dns_ip;


    public Operation(String client_ip, String domain, String time, String target_ip, String rcode, String query_type, String auhority_record, String add_msg, String dns_ip) {
        this.client_ip = client_ip;
        this.domain = domain;
        this.time = time;
        this.target_ip = target_ip;
        this.rcode = rcode;
        this.query_type = query_type;
        this.auhority_record = auhority_record;
        this.add_msg = add_msg;
        this.dns_ip = dns_ip;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getQuery_type() {
        return query_type;
    }

    public String getAdd_msg() {
        return add_msg;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getAuhority_record() {
        return auhority_record;
    }

    public void setAuhority_record(String auhority_record) {
        this.auhority_record = auhority_record;
    }

    public String getDns_ip() {
        return dns_ip;
    }

    public void setDns_ip(String dns_ip) {
        this.dns_ip = dns_ip;
    }

    public static List<Operation> generateOperations(int rowNums){
        List<Operation> operationList = new ArrayList<>();
        for (int i = 0; i < rowNums; i++){
            String client_ip = UUID.randomUUID().toString();
            String time = String.valueOf(System.currentTimeMillis());
            String domain = RandomStringUtils.randomAlphabetic(20);
            String target_ip = RandomStringUtils.randomAlphabetic(15);
            String rcode = RandomStringUtils.randomAlphabetic(8);
            String auhority_record = RandomStringUtils.randomAlphabetic(12);
            String query_type = RandomStringUtils.randomAlphabetic(5);
            String add_msg = RandomStringUtils.randomAlphabetic(10);
            String dns_ip = RandomStringUtils.randomAlphabetic(10);

            Operation operation = new Operation(client_ip,domain,time,target_ip,rcode,query_type,auhority_record,add_msg,dns_ip);
            operationList.add(operation);
        }

        return operationList;
    }


}
