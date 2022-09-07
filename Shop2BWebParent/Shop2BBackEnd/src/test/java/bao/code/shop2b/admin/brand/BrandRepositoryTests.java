package bao.code.shop2b.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.lowagie.text.Cell;

import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellphones = new Category(4);
		Category tablet = new Category(7);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablet);
		
		Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {	
		Brand samsung = new Brand("Samsung");
		
		samsung.getCategories().add(new Category(1));
		samsung.getCategories().add(new Category(2));
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetByID() {
		Brand brand = repo.findById(1).get();
		
		assertThat(brand.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(3).get();
		
		samsung.setName(newName);
		
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id=2;
		repo.deleteById(2);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result).isEmpty();
	}
}


