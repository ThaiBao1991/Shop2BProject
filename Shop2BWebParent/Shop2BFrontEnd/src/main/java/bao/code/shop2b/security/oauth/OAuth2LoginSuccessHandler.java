package bao.code.shop2b.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import bao.code.shop2b.common.entity.AuthenticationType;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.customer.CustomerService;
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private CustomerService customerService;
	
	@Autowired 
	public OAuth2LoginSuccessHandler(@Lazy CustomerService customerService) {
		this.customerService=customerService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();
		
		String name = oAuth2User.getName();
		String email = oAuth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		
		System.out.println("OAuth2LoginSuccessHanlder : "+ name + " | " + email);
		
		Customer customer = customerService.getCustomerByEmail(email);
		if(customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name,email,countryCode);
		}else {
			customerService.updateAuthentication(customer, AuthenticationType.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
