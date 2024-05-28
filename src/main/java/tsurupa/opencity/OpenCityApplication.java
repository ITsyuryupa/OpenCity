package tsurupa.opencity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class OpenCityApplication {

    public static void main(String[] args) {

        SpringApplication.run(OpenCityApplication.class, args);
    }

}
