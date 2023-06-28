package tech.jazz.apicardholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiPortadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPortadorApplication.class, args);
    }

}
