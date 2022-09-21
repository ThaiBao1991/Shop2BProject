package bao.code.shop2b.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import bao.code.shop2b.category.CategoryService;
import bao.code.shop2b.common.entity.Category;
import bao.code.shop2b.common.entity.Product;
import bao.code.shop2b.common.exception.CategoryNotFoundException;

@Controller
public class ProductController {
	@Autowired	private CategoryService categoryService;
	
	@Autowired	private ProductService productService;
	

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		try {
			Category category = categoryService.getCategory(alias);
			
			List<Category> listCategoryParents = categoryService.getCategoryParents(category);
			
			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProducts.getContent();
			
			long startCount =(pageNum -1)*ProductService.PRODUCTS_PER_PAGE +1 ;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE -1;
			
			if(endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			
			
			model.addAttribute("currentPage",pageNum);
			model.addAttribute("totalPages",pageProducts.getTotalPages());
			model.addAttribute("startCount",startCount);
			model.addAttribute("endCount",endCount);
			model.addAttribute("totalItems",pageProducts.getTotalElements());
			
			model.addAttribute("pageTitle",category.getName());
			model.addAttribute("listCategoryParents",listCategoryParents);
			model.addAttribute("listProducts",listProducts);
			model.addAttribute("category",category);
			
			return "products_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
		
	}
}
