package bao.code.shop2b.admin.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bao.code.shop2b.admin.FileUploadUtil;
import bao.code.shop2b.admin.category.CategoryService;
import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.admin.paging.PagingAndSortingParam;
import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.entity.Category;
import bao.code.shop2b.common.exception.BrandNotFoundException;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage(RedirectAttributes ra,@ModelAttribute("message") final String message) {
		if(message.length()>1) {
			ra.addFlashAttribute("message",message);
			}
		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands",moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") int pageNum
			) {
		
		brandService.listByPage(pageNum,helper);
		
		
		return "brands/brand";
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInform();
		
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle","Create New Brand");
		
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand,
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.save(brand);
			String uploadDir ="../brand-logos/" +savedBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		ra.addFlashAttribute("message","The Brand has been saved successfully");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name="id") Integer id
			, Model model, RedirectAttributes ra) throws BrandNotFoundException{
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInform();
			
			model.addAttribute("brand",brand);
			model.addAttribute("listCategories",listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID : " +id +")");
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name="id") Integer id,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir= "../brand-logos/" +id;
			FileUploadUtil.removeDir(brandDir);
			
			redirectAttributes.addFlashAttribute("message",
					"The brand ID "+ id +" has been deleted successfully");
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
		}
		
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException{
		List<Brand> listBrands = brandService.listAll();
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrands, response);
	}
}
