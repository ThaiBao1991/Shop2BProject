package bao.code.shop2b.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {
	@MockBean
	private BrandRepository repo;
	
	@InjectMocks
	private BrandService service;
	
	@Test
	public void testCheckUniqueInNewModelReturnDuplicate() {
		Integer id =null;
		String name = "Acer";
		Brand brand = new Brand(name);
		
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, name);
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModelReturnOK() {
		Integer id =null;
		String name = "AMD";
		
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		
		String result = service.checkUnique(id, name);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModelReturnDuplicate() {
		Integer id =1;
		String name = "Canon";
		Brand brand = new Brand(id,name);
		
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(2, "Canon");
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	
	@Test
	public void testCheckUniqueInEditModelReturnOK() {
		Integer id =1;
		String name = "Acer";
		Brand brand = new Brand(id,name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(1, "Canon");
		
		assertThat(result).isEqualTo("OK");
	}
}
