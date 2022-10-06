package bao.code.shop2b.admin.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import bao.code.shop2b.admin.AbstractExporter;
import bao.code.shop2b.common.entity.Customer;

public class CustomerCsvExporter extends AbstractExporter{
public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {
	super.setResponseHeader(response,"text/csv", ".csv","customers_");
	
	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader = {"Customer ID","Customer First Name","Customer Last Name",
			"Customer Phone Number","Customer Address","Customer City","Customer State","Postal Code"};
	String[] fieldMapping = {"id","firstName","lastName","phoneNumber","addressLine1","city","state","postalCode"};
	
	csvWriter.writeHeader(csvHeader);
	
	for(Customer customer : listCustomers) {
		customer.setFirstName(customer.getFirstName());
		customer.setLastName(customer.getLastName());
		csvWriter.write(customer, fieldMapping);
	}
	csvWriter.close();
}
}
