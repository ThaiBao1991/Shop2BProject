package bao.code.shop2b.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication()
@EntityScan({"bao.code.shop2b.common.entity","bao.code.shop2b.admin.user"})
public class Shop2BBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(Shop2BBackEndApplication.class, args);
	}

}
