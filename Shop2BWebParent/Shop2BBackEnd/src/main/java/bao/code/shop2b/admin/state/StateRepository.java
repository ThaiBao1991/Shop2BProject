package bao.code.shop2b.admin.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
}
