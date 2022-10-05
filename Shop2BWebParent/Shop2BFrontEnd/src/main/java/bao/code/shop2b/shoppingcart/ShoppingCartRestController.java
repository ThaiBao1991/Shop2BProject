package bao.code.shop2b.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import bao.code.shop2b.Utility;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.exception.CustomerNotFoundException;
import bao.code.shop2b.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name="productId") Integer productId,
			@PathVariable("quantity") Integer quantity,HttpServletRequest request) throws ShoppingCartException {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updateQuantity = cartService.addProduct(productId, quantity, customer);
			return updateQuantity +" item(s) of this product were add to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart";
		}catch(ShoppingCartException ex) {
			return ex.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name="productId") Integer productId,
			@PathVariable("quantity") Integer quantity,HttpServletRequest request)  {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product";
		}
	}
}
