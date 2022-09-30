package bao.code.shop2b.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import bao.code.shop2b.admin.paging.PagingAndSortingArgumentResolver;

/**
 * @author bao
 *
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		User photo
		exposeDirectory("user-photos",registry);	
//		Category Image
		exposeDirectory("../category-images",registry);		
//		Logo Brand
		exposeDirectory("../brand-logos",registry);	
//		Product Main Image 
		exposeDirectory("../product-images",registry);
//		Product site logo 
		exposeDirectory("../site-logo",registry);	
	}
	
	private void exposeDirectory(String pathPattern ,  ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern.replace("..", "")+"/**";
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + absolutePath+ "/");
		
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
	
}
