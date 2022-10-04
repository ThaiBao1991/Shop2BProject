package bao.code.shop2b.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import bao.code.shop2b.Utility;
import bao.code.shop2b.common.entity.Customer;
import bao.code.shop2b.common.exception.CustomerNotFoundException;
import bao.code.shop2b.setting.EmailSettingBag;
import bao.code.shop2b.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request) + "/reset_password?token=" +token;
			sendEmail(link, email);
			model.addAttribute("message", "We have sent a reset password link to your email." +
			" Please check .");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException |MessagingException e) {
				model.addAttribute("error","Could not send email");
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Here's is the link to reset your password";
		
		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password</p>"
				+ "<p>Click the link below to change your password</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<p>Ignore this email if you do remember your password , or you have not made the request.</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAdress(),emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
	
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if(customer !=null) {
			model.addAttribute("token",token);
		}else {
			model.addAttribute("pageTitle","Invalid token");
			model.addAttribute("message","Invalid token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle","Reset Your Password");
			model.addAttribute("title","Reset Your Password");
			model.addAttribute("message", "You have successfully changed your password");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message",e.getMessage());
			return "message";
		}
	}
}
	