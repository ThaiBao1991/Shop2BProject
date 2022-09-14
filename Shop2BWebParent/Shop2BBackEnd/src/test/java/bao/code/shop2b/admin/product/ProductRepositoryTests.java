package bao.code.shop2b.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;
import bao.code.shop2b.common.entity.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void TestCreateProduct() {
		
		Brand brand = entityManager.find(Brand.class,1);
		Category category =entityManager.find(Category.class, 1);
		
		Product product = new Product();
		product.setName("Samsung Glaxy A3");
		product.setAlias("Samsung_Glaxy_A3");
		product.setShortDescription("A good prodcut	short description");
		product.setFullDescription("A good prodcut full description");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(341);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreateTime(new Date());
		product.setUpdateTime(new Date());
		
//		Add sau khi tao mainImage
		product.setMainImage("mainImage");
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
//		Create product id 2
		brand = entityManager.find(Brand.class,2);
		category =entityManager.find(Category.class, 2);
		
		product = new Product();
		product.setName("Dell Inspirison 300");
		product.setAlias("Dell_Inspirison_300");
		product.setShortDescription("A good prodcut	short description");
		product.setFullDescription("A good prodcut full description");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(341);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreateTime(new Date());
		product.setUpdateTime(new Date());
		
//		Add sau khi tao mainImage
		product.setMainImage("mainImage");
		
		savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
//		Create product id 3
		brand = entityManager.find(Brand.class,3);
		category =entityManager.find(Category.class, 3);
		
		product = new Product();
		product.setName("Apple X");
		product.setAlias("Apple_X");
		product.setShortDescription("A good prodcut	short description");
		product.setFullDescription("A good prodcut full description");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(341);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreateTime(new Date());
		product.setUpdateTime(new Date());
		
//		Add sau khi tao mainImage
		product.setMainImage("mainImage");
		
		savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id =2;
		Product product = repo.findById(id).get();
		
		System.out.println(product);
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id=1;
		Product product = repo.findById(id).get();
		
		product.setPrice(599);
		
		repo.save(product);
		
		Product updateProduct = entityManager.find(Product.class, id);
		
		assertThat(updateProduct.getPrice()).isEqualTo(599);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id =3;
		repo.deleteById(id);
		
		Optional<Product> result = repo.findById(id);
		
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId=1;
		Product product =repo.findById(productId).get();
		
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image_1.jpg");
		product.addExtraImage("main_image_2.jpg");
		product.addExtraImage("main-image3.jpg");
		
		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
}


