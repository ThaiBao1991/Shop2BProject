package bao.code.shop2b.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
	
	@Query("SELECT c fROM Country c WHERE c.code =?1")
	public Country findByCode(String code);
}
