package bao.code.shop2b.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Shop2BFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(Shop2BFrontEndApplication.class, args);
	}

}
