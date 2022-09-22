package bao.code.shop2b.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.Setting;
import bao.code.shop2b.common.entity.SettingCategory;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
	
	@Autowired SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		Setting siteName = new Setting("SITE_NAME","Shop2B",SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO","Shop2B.png",SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT","Copyright (C) 2022 Shop2B Ltd",SettingCategory.GENERAL);
		
		Setting savedSetting = repo.save(siteName);
		
		List<Setting> addSettingList = new ArrayList<Setting>();
		addSettingList.add(siteLogo);
		addSettingList.add(copyright);
		
		repo.saveAll(Collections.unmodifiableList(addSettingList));
		
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currentId = new Setting("CURRENT_ID","1",SettingCategory.CURRENCY);
		Setting symbol = new Setting("SYMBOL","$",SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENT_SYMBOL_POSITION","before",SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","POINT",SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS","2",SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE","COMMA",SettingCategory.CURRENCY);
		
		List<Setting> addSettingList = new ArrayList<Setting>();
		
		addSettingList.add(currentId);
		addSettingList.add(symbol);
		addSettingList.add(symbolPosition);
		addSettingList.add(decimalPointType);
		addSettingList.add(decimalDigits);
		addSettingList.add(thousandsPointType);
		
		repo.saveAll(Collections.unmodifiableList(addSettingList));
		
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
}


