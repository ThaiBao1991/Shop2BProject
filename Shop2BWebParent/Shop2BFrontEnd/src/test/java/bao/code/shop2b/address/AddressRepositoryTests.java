package bao.code.shop2b.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.Address;
import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;


@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	
	@Autowired private AddressRepository repo;
	
	@Test
	public void testAddNew() {
		Integer customerId =1;
		Integer countryId = 234; // USA
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Charles");
		newAddress.setLastName("Brugger Ronin");
		newAddress.setPhoneNumber("646-232-3202");
		newAddress.setAddressLine1("204 MorningView Lane");
		newAddress.setCity("New York");
		newAddress.setState("New York");
		newAddress.setPostalCode("10013");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId =5;
		List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);
		
		listAddresses.forEach(System.out ::	println);
	}
	
	@Test
	public void testFindByIdAndCustomer() {
		Integer addresssId =1;
		Integer customerId = 5;
		
		Address address = repo.findByIdAndCustomer(addresssId, customerId);
		
		assertThat(address).isNotNull();
		System.out.println(address);
	}
	
	@Test
	public void testUpdate() {
		Integer addressId =2;
		String phoneNumber ="646-232-3832";
		
		Address address = repo.findById(addressId).get();
		address.setPhoneNumber(phoneNumber);
		address.setDefaultForShipping(true);
		
		Address updateAddress = repo.save(address);
		
		assertThat(updateAddress.getPhoneNumber()).isEqualTo(phoneNumber);
	}
	
	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 1 ;
		Integer customerId = 5 ;
		
		repo.deleteByIdAndCustomer(addressId, customerId);
		
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNull();
	}
	
	@Test
	public void testSetDefault() {
		Integer addressId = 4;
		repo.setDefaultAdress(addressId);
		
		Address address = repo.findById(addressId).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefaultAddresses() {
		Integer addressId =3;
		Integer customerId =1;
		repo.setNonDefaultForOther(addressId, customerId);
	}
 }
