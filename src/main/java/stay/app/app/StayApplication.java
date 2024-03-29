package stay.app.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StayApplication {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");  // 추가
    }

    public static void main(String[] args) {
        SpringApplication.run(StayApplication.class, args);
    }

}
