package bao.code.shop2b.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;
import bao.code.shop2b.common.exception.BrandNotFoundException;

@RestController
public class BrandRestController {
	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(Integer id, String name, String alias) {
		return service.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name="id") Integer brandId) throws BrandNotFoundRestException{
		List<CategoryDTO> listCategories = new ArrayList<CategoryDTO>();
		try {
			Brand brand = service.get(brandId);
			Set<Category> categories = brand.getCategories();
			for(Category category : brand.getCategories()) {
				CategoryDTO dto = new CategoryDTO(category.getId(),category.getName());
				listCategories.add(dto);
			}
			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
