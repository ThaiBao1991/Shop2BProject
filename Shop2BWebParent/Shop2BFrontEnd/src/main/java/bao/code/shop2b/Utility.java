package bao.code.shop2b;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import bao.code.shop2b.setting.EmailSettingBag;

public class Utility {
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		
		return siteURL.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPost());
		mailSender.setUsername(settings.getUserName());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
}
