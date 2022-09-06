package bao.code.shop2b.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import bao.code.shop2b.admin.AbstractExporter;
import bao.code.shop2b.common.entity.User;

public class UserCsvExporter extends AbstractExporter{
public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
	super.setResponseHeader(response,"text/csv", ".csv","users_");
	
	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader = {"User ID", "E-mail","First Name",
			"Last Name","Gender","Roles","Enabled"};
	String[] fieldMapping = {"id","email","firstName",
			"lastName","gender","roles","enabled"};
	
	csvWriter.writeHeader(csvHeader);
	
	for(User user : listUsers) {
		csvWriter.write(user, fieldMapping);
	}
	csvWriter.close();
}
}
