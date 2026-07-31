package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockBrokerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockBrokerBackendApplication.class, args);
	}

}
