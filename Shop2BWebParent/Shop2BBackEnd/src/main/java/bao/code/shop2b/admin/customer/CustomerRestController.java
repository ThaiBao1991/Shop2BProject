package bao.code.shop2b.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;
	
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id
			, @Param("name") String name) {
		if(service.isEmailUnique(id, name)) {
			return "OK";
		}else {
			return "Duplicated";
		}
	}
}
