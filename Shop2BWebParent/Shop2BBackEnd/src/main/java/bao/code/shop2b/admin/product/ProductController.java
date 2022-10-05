package bao.code.shop2b.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bao.code.shop2b.admin.FileUploadUtil;
import bao.code.shop2b.admin.brand.BrandService;
import bao.code.shop2b.admin.category.CategoryService;
import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.admin.paging.PagingAndSortingParam;
import bao.code.shop2b.admin.security.ShopUserDetails;
import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;
import bao.code.shop2b.common.entity.Product;
import bao.code.shop2b.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(RedirectAttributes ra,@ModelAttribute("message") final String message) {
		if(message.length()>1) {
			ra.addFlashAttribute("message",message);
			}
		return "redirect:/products/page/1?sortField=id&sortDir=asc";
	}
	
	@GetMapping("/products/page/{pageNum}")	
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") int pageNum,
			Model model,
			@Param("categoryId") Integer categoryId
			) {
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInform();
		if(categoryId!=null) model.addAttribute("categoryId",categoryId);
		model.addAttribute("listCategories",listCategories);
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product",product);
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("pageTitle","Create New Product");
		model.addAttribute("numberOfExistingExtraImages",0);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product,
			@RequestParam(value="fileImage", required=false) MultipartFile mainImageMultipart,
			@RequestParam(value="extraImage", required=false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name= "detailIDs", required=false) String[] detailIDs,
			@RequestParam(name= "detailNames", required=false) String[] detailNames,
			@RequestParam(name= "detailValues", required=false) String[] detailValues,
			@RequestParam(name= "imageIDs", required=false) String[] imageIDs,
			@RequestParam(name= "imageNames", required=false) String[] imageNames,
			@AuthenticationPrincipal ShopUserDetails loggedUser,
			RedirectAttributes ra) throws IOException {
		
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message","The product has been saved successfully");
				return "redirect:/products";
			}
			
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageName(imageIDs,imageNames,product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts,product);
		ProductSaveHelper.setProductDetails(detailIDs,detailNames,detailValues,product);
			
		Product savedProduct = productService.save(product);
		
		ProductSaveHelper.saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		ra.addFlashAttribute("message","The product has been saved successfully");
		
		return "redirect:/products";
	}
		
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message ="The category ID " +id + " has been " + status;
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteCategory(@PathVariable(name="id") Integer id,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productExtraImagesDir ="../product-images/" +id +"/extras";
			String productDir= "../products-images/" +id;
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productDir);
			
			redirectAttributes.addFlashAttribute("message",
					"The product ID "+ id +" has been deleted successfully");
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
		}
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable(name="id") Integer id
			, Model model, RedirectAttributes ra,
			@AuthenticationPrincipal ShopUserDetails loggedUser) throws ProductNotFoundException{
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			boolean isReadOnlyForSalesperson=false;
			
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesperson=true;
				}
				
			}
			
			model.addAttribute("isReadOnlyForSalesperson",isReadOnlyForSalesperson);
			model.addAttribute("product",product);
			model.addAttribute("listBrands",listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID : " +id +")");
			model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
			
			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable(name="id") Integer id
			, Model model, RedirectAttributes ra) throws ProductNotFoundException{
		try {
			Product product = productService.get(id);
			model.addAttribute("product",product);
			
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException{
		List<Product> listProducts = productService.listAll();
		ProductCsvExporter exporter = new ProductCsvExporter();
		exporter.export(listProducts, response);
	}
}
