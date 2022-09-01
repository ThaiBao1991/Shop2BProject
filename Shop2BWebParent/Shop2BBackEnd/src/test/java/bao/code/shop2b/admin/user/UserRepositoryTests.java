package bao.code.shop2b.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.Role;
import bao.code.shop2b.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1); 
		User userthaibao= new User("thaibao1991dn@gmail.com","bao1991","Thai","Bao","Male");
		userthaibao.addRole(roleAdmin);
		
		User savedUser =repo.save(userthaibao);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateUserWithTwoRoles() {
		User userYNhi = new User("YNhi@gmail.com","YNhi95","Y","Nhi","Female");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userYNhi.addRole(roleEditor);
		userYNhi.addRole(roleAssistant);
		
		User saveUser = repo.save(userYNhi);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user ->System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userThaiBao = repo.findById(1).get();
		System.out.println(userThaiBao);
		assertThat(userThaiBao).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userThaiBao = repo.findById(1).get();
		userThaiBao.setEnabled(true);
		
		repo.save(userThaiBao);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userYNhi = repo.findById(2).get();
		Role roleEditor = new Role(3);
		userYNhi.getRoles().remove(roleEditor);
		
		Role roleSaleperson = new Role(2);
		userYNhi.addRole(roleSaleperson);
		
		repo.save(userYNhi);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId=10;
		repo.deleteById(userId);
	}
	@Test
	public void testGetUserByEmail() {
		String email ="thaibao1991dn@gmail.com";
		User user =repo.getUserByEmail(email);
		System.out.println(user);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id=1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDiableUser() {
		Integer id=1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id=1;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber =0;
		int pageSize =4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUser = page.getContent();
		
		listUser.forEach(user -> System.out.println(user));
		
		assertThat(listUser.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword ="ai";
		
		int pageNumber =0;
		int pageSize =4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		
		List<User> listUser = page.getContent();
		
		listUser.forEach(user -> System.out.println(user));
		
		assertThat(listUser.size()).isGreaterThan(0);
	}
}
