package bao.code.shop2b.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Setting;
import bao.code.shop2b.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
}
