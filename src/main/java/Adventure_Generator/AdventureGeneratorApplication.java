package Adventure_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class AdventureGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdventureGeneratorApplication.class, args);
	}

}
