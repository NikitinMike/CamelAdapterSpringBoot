package camel.springboot.msg.adapter;

import static java.lang.Math.random;
import static java.lang.Math.round;

import camel.springboot.msg.adapter.data.Coordinates;
import camel.springboot.msg.adapter.data.MsgA;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdapterMain {

    public static void main(String[] args) {
        SpringApplication.run(AdapterMain.class, args);
    }

}
