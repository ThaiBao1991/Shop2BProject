package bao.code.shop2b.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.setting.CountryRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService {
	@Autowired private CountryRepository countryRepo;
	@Autowired private CustomerRepository customerRepo;
	@Autowired PasswordEncoder passwordEncoder;
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreateTime(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		
		System.out.println("Verification code : " +randomCode);
		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		
	}
}
