package bao.code.shop2b.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.Address;
import bao.code.shop2b.common.entity.Customer;

@Service
public class AddressService {
	@Autowired private AddressRepository repo;
	
	public List<Address> listAddressBook (Customer customer){
		return repo.findByCustomer(customer);
	}
}
