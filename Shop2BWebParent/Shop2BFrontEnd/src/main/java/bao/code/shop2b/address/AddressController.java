package bao.code.shop2b.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bao.code.shop2b.Utility;
import bao.code.shop2b.common.entity.Address;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.customer.CustomerService;


@Controller
public class AddressController {
	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	
	@GetMapping("/address_book")
		public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		boolean usePrimaryAddressAsDefault = true;
		for(Address address : listAddresses) {
			if(address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}
		
		model.addAttribute("listAddresses",listAddresses);
		model.addAttribute("customer",customer);
		model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
		
		return "address_book/addresses";
		}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
