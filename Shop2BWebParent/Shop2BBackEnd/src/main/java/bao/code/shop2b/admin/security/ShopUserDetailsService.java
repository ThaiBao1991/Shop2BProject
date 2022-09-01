package bao.code.shop2b.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import bao.code.shop2b.admin.user.UserRepository;
import bao.code.shop2b.common.entity.User;

public class ShopUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user =userRepo.getUserByEmail(email);
		
		if(user != null) {
			return new ShopUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email : " +email);
	}

}
