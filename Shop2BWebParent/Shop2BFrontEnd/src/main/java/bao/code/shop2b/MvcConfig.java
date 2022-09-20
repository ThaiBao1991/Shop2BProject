package bao.code.shop2b;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bao
 *
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {	
//		Category Image
		exposeDirectory("../category-images",registry);		
//		Logo Brand
		exposeDirectory("../brand-logos",registry);	
//		Product Main Image 
		exposeDirectory("../product-images",registry);	
	}
	
	private void exposeDirectory(String pathPattern ,  ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern.replace("..", "")+"/**";
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + absolutePath+ "/");
		
	}
}
