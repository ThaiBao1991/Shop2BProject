package bao.code.shop2b.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
	
	public List<Currency> findAllByOrderByNameAsc();
}
