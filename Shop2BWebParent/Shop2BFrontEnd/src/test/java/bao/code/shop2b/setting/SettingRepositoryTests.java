package bao.code.shop2b.setting;

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
	public void testFindByTwoCategories() {
		List<Setting> settings = repo.findByTwoCategories(SettingCategory.GENERAL,
				SettingCategory.CURRENCY);
		settings.forEach(System.out :: println);
	}
}


