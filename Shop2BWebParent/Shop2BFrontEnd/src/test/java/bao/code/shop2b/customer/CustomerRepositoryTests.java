package bao.code.shop2b.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	@Autowired
	private CustomerRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId =236; //USA
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Backam");
		customer.setPassword("12341234");
		customer.setEmail("davidbackam@yahoo.com");
		customer.setPhoneNumber("312-462-7518");
		customer.setAddressLine1("Ngoai O");
		customer.setAddressLine2("Da Nang");
		customer.setCity("Da Nang");
		customer.setState("California");
		customer.setPostalCode("95873");
		customer.setVerificationCode("code1234");
		customer.setCreateTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out ::println);
		
		assertThat(customers).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId =1;
		String lastName ="InterMilan";
		String verificationCode = "abc123";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setVerificationCode(verificationCode);
		customer.setEnabled(true);
		
		Customer updateCustomer = repo.save(customer);
		
		assertThat(updateCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testFindByEmail() {
		String email ="davidbackam@yahoo.com";
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code ="abc123";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId =1;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId =1;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isNotPresent();
	}
}


