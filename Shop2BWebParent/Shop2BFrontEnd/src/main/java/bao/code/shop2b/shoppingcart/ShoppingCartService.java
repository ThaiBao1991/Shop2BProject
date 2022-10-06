package bao.code.shop2b.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.CartItem;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.entity.Product;
import bao.code.shop2b.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	@Autowired private ProductRepository productRepo;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException{
		Integer updateQuantity = quantity;
		Product product = new Product(productId);
				
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		if(cartItem !=null) {
			updateQuantity = cartItem.getQuantity() + quantity;
			if(updateQuantity >5) {
				throw new ShoppingCartException("Could not add more " + quantity + " item(s) "+
			"because there's already " +cartItem.getQuantity() + "item(s)" 
				+ "in your shopping cart. Maximum allowed quantity is 5.");
			}
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updateQuantity);
		
		cartRepo.save(cartItem);
		
		return updateQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer){
		return cartRepo.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepo.findById(productId).get();
		float subtotal = product.getDiscountPrice() * quantity;
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
	}
}
