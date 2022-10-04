package bao.code.shop2b.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.CartItem;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.entity.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {
	@Autowired private CartItemRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerid = 1;
		Integer productid  =1;
		
		Customer customer = entityManager.find(Customer.class, customerid);
		Product product = entityManager.find(Product.class, productid);
		
		CartItem newCartItem = new CartItem();
		newCartItem.setCustomer(customer);
		newCartItem.setProduct(product);
		newCartItem.setQuantity(1);
		
		CartItem savedCartItem = repo.save(newCartItem);
		
		assertThat(savedCartItem.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSave2Item() {
		Integer customerid = 2;
		Integer productid  =2;
		
		Customer customer = entityManager.find(Customer.class, customerid);
		Product product = entityManager.find(Product.class, productid);
		
		CartItem item1 = new CartItem();
		item1.setCustomer(customer);
		item1.setProduct(product);
		item1.setQuantity(2);
		
		CartItem item2 = new CartItem();
		item2.setCustomer(new Customer(customerid));
		item2.setProduct(new Product(8));
		item2.setQuantity(3);
		
		List<CartItem> listCartItems = new ArrayList<CartItem>();
		listCartItems.add(item1);
		listCartItems.add(item2);
		
		Iterable<CartItem> iterable = repo.saveAll(Collections.unmodifiableList(listCartItems));
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 2;
		List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));
		listItems.forEach(System.out :: println);
		
		assertThat(listItems.size()).isEqualTo(2);
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId =1;
		Integer productId = 1;
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNotNull();
		
		System.out.println(item);
	}
	
	@Test
	public void testUpdateQuantity() { 
		Integer customerId =1 ;
		Integer productId =1 ;
		Integer quantity =4 ;
		
		repo.updateQuantity(quantity, customerId, productId);
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		assertThat(item.getQuantity()).isEqualTo(4);
	}
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer customerId =1;
		Integer productId =1;
		
		repo.deleteByCustomerAndProduct(customerId, productId);
		
		CartItem item =repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNull();
	}
}
