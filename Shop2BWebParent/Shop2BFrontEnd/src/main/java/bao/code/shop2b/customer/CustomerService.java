package bao.code.shop2b.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.setting.CountryRepository;

@Service
public class CustomerService {
	@Autowired private CountryRepository countryRepo;
	@Autowired private CustomerRepository customerRepo;
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
}
