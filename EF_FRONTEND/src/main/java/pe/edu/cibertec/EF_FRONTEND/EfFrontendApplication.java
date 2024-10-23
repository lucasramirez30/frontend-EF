package pe.edu.cibertec.EF_FRONTEND;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EfFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EfFrontendApplication.class, args);
	}

}
