package sistema.Academia_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AcademiaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademiaApiApplication.class, args);

		System.out.println("rodando");
	}

}
