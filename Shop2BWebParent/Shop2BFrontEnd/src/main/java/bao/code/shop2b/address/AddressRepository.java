package bao.code.shop2b.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Address;
import bao.code.shop2b.common.entity.Customer;

public interface AddressRepository extends CrudRepository<Address, Integer>{
	public List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	public Address findByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Modifying
	@Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	public void deleteByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
	@Modifying
	public void setDefaultAdress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false" + 
	" WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultForOther(Integer defaultAddressId, Integer customerId);
}
