package bao.code.shop2b.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import bao.code.shop2b.admin.AbstractExporter;
import bao.code.shop2b.common.entity.Brand;

public class ProductCsvExporter extends AbstractExporter{
public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
	super.setResponseHeader(response,"text/csv", ".csv","brands_");
	
	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader = {"Brand ID","Brand Name","Brand Categories"};
	String[] fieldMapping = {"id","name","categories"};
	
	csvWriter.writeHeader(csvHeader);
	
	for(Brand brand : listBrands) {
		brand.setName(brand.getName());
		csvWriter.write(brand, fieldMapping);
	}
	csvWriter.close();
}
}
