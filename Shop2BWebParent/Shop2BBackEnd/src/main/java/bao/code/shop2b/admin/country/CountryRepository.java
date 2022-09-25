package bao.code.shop2b.admin.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
}
