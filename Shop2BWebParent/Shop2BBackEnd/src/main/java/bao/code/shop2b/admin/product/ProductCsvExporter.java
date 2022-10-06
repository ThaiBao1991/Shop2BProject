package bao.code.shop2b.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import bao.code.shop2b.admin.AbstractExporter;
import bao.code.shop2b.common.entity.Product;

public class ProductCsvExporter extends AbstractExporter{
public void export(List<Product> listProducts, HttpServletResponse response) throws IOException {
	super.setResponseHeader(response,"text/csv", ".csv","products_");
	
	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader = {"Product ID","Product Name","Product Brand","Product Categories"};
	String[] fieldMapping = {"id","name","brand","category"};
	
	csvWriter.writeHeader(csvHeader);
	
	for(Product product : listProducts) {
		product.setName(product.getName());
		csvWriter.write(product, fieldMapping);
	}
	csvWriter.close();
}
}
