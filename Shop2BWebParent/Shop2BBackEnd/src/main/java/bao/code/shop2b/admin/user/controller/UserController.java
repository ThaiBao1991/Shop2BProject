package bao.code.shop2b.admin.user.controller;

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
import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.admin.paging.PagingAndSortingParam;
import bao.code.shop2b.admin.user.UserService;
import bao.code.shop2b.admin.user.export.UserCsvExporter;
import bao.code.shop2b.admin.user.export.UserExcelExporter;
import bao.code.shop2b.admin.user.export.UserPdfExporter;
import bao.code.shop2b.common.entity.Role;
import bao.code.shop2b.common.entity.User;
import bao.code.shop2b.common.exception.UserNotFoundException;

@Controller
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(RedirectAttributes ra,@ModelAttribute("message") final String message) {
		if(message.length()>1) {
		ra.addFlashAttribute("message",message);
		}
		return "redirect:/users/page/1?sortField=id&sortDir=asc";
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") int pageNum
			) {
		
 		service.listByPage(pageNum, helper);
		return("users/users");
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user= new User();
		user.setEnabled(true);
		
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle","Create New User");
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser = service.save(user);
			
			String uploadDir ="user-photos/" +saveUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty())
				user.setPhotos(null);
				service.save(user);
		}	
		redirectAttributes.addFlashAttribute("message","The user has been save successfully");
		
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartOfEmail;
	}
	
	@GetMapping("users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			
			model.addAttribute("listRoles",listRoles);
			model.addAttribute("user",user);
			model.addAttribute("pageTitle","Edit User (ID : " +id +")");
			
			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") Integer id,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The user Id = " +id +" has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());		
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled , RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message ="The user ID "+ id + " has been " + status;
		redirectAttributes.addFlashAttribute("message",message);
		
		return "redirect:/users";
	}
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers =service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers =service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers =service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
}
