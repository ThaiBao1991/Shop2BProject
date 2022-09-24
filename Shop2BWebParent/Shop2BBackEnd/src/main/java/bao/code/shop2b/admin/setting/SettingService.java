package bao.code.shop2b.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.common.entity.GeneralSettingBag;
import bao.code.shop2b.common.entity.Setting;
import bao.code.shop2b.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired private SettingRepository repo;
	
	public List<Setting> listAllSettings(){
		return (List<Setting>) repo.findAll();
	}
	
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> generalSettings = new ArrayList<Setting>();
		
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> curreSettings = repo.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(curreSettings);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}
}
