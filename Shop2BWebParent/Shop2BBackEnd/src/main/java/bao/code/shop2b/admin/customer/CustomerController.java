package bao.code.shop2b.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.exception.CustomerNotFoundException;

@Controller
public class CustomerController {
	@Autowired private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(model, 1, "firstName", "asc", null);
	}
	
	@GetMapping("/customers/new")
	public String newProduct(Model model) {
		List<Customer> listCustomers = service.listAll();
		List<Country> listCountries = service.listAllCountries();
		
		Customer customer = new Customer();
		
		model.addAttribute("customer",customer);
		model.addAttribute("listCustomers",listCustomers);
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle","Create New Customer");
		
		return "customers/customer_form";
	}
	
	/**
	 * @param model
	 * @param pageNum
	 * @param sortField
	 * @param sortDir
	 * @param keyword
	 * @return
	 */
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(Model model
			,@PathVariable(name="pageNum") int pageNum		
			,@Param("sortField") String sortField
			,@Param("sortDir") String sortDir
			,@Param("keyword") String keyword
			) {
		Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();

		
		long startCount =(pageNum -1)*CustomerService.CUSTOMER_PER_PAGE +1 ;
		long endCount = startCount + CustomerService.CUSTOMER_PER_PAGE -1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" :"asc";
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);
		model.addAttribute("listCustomers",listCustomers);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes) {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message ="The customer ID " +id + " has been " + status;
		redirectAttributes.addFlashAttribute("message",message);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable(name="id") Integer id
			, Model model, RedirectAttributes ra) throws CustomerNotFoundException{
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer",customer);

			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable(name="id") Integer id
			, Model model, RedirectAttributes ra) throws CustomerNotFoundException{
		try {
			Customer customer = service.get(id);
			List<Country> listCountries = service.listAllCountries();
			
			model.addAttribute("listCountries",listCountries);
			model.addAttribute("customer",customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID : %d)", id));
			
			return "customers/customer_form";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		service.save(customer);
		ra.addFlashAttribute("message","The customer ID = "+ customer.getId() + "  has been updated successfully");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			service.delete(id);
			ra.addFlashAttribute("message","The customer ID = "+ id + "  has been deleted successfully");
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
		}
		return "redirect:/customers";
	}
}
