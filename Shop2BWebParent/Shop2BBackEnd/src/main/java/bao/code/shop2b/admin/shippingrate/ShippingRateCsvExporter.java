package bao.code.shop2b.admin.shippingrate;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import bao.code.shop2b.admin.AbstractExporter;
import bao.code.shop2b.common.entity.ShippingRate;

public class ShippingRateCsvExporter extends AbstractExporter{
public void export(List<ShippingRate> listShippingRate, HttpServletResponse response) throws IOException {
	super.setResponseHeader(response,"text/csv", ".csv","shipping_rates_");
	
	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader = {"Shipping Rate ID","Country","State",
			"Rate","Days","COD Supported"};
	String[] fieldMapping = {"id","country","state","rate","days","codSupported"};
	
	csvWriter.writeHeader(csvHeader);
	
	for(ShippingRate rate : listShippingRate) {
		rate.setCountry(rate.getCountry());
		csvWriter.write(rate, fieldMapping);
	}
	csvWriter.close();
}
}
