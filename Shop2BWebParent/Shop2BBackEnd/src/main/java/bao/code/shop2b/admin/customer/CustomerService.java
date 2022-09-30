package bao.code.shop2b.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bao.code.shop2b.admin.country.CountryRepository;
import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {
	
	public static final int CUSTOMER_PER_PAGE =10;
	
	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	
	public List<Customer> listAll (){
		return (List<Customer>) customerRepo.findAll();
	}
	
	public void listByPage(int pageNum ,PagingAndSortingHelper helper){
		helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException{
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customers with ID : " +id);
		}
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);
		
		if(existCustomer !=null && existCustomer.getId() !=id) {
//			found another customer have the same email
			return false;
		}
		return true;
	}
	
	public void save(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		if(!customerInForm.getPassword().isEmpty()) {
			String encodePassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodePassword);
		}else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreateTime(customerInDB.getCreateTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException{
		Long count = customerRepo.countById(id);
		if(count==null || count ==0) {
			throw new CustomerNotFoundException("Could not find any customers with ID : " +id);
		}
		
		customerRepo.deleteById(id);
	}
}
