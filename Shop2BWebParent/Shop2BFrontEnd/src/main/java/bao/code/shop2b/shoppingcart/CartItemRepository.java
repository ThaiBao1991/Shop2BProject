package bao.code.shop2b.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.CartItem;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.entity.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
	public List<CartItem> findByCustomer(Customer customer);
	
	public CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c set c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	public void updateQuantity(Integer quantity, Integer customerId, Integer productId);
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}
