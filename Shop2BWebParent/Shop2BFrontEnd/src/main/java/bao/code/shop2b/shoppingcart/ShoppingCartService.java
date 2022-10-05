package bao.code.shop2b.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.CartItem;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.entity.Product;

@Service
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	
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
}
