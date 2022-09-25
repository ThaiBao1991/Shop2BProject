package bao.code.shop2b.admin.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.State;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
	@Autowired
	private StateRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId =2;
		Country country = entityManager.find(Country.class, countryId);
		
		List<State> listStates = Arrays.asList(new State("Karnakata",country),
				new State("Punjab",country),
				new State("Uttar Pradesh",country),
				new State("West Bengal",country)
				);
		
		repo.saveAll(listStates);
		
		Iterable<State> iterable = repo.findAll();
		
		assertThat(iterable).size().isEqualTo(4);
		
	}
	
	@Test
	public void testListStatesByCountry() {
		Integer countryId =2;
		Country country = entityManager.find(Country.class, countryId);
		
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		listStates.forEach(System.out ::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		Integer stateId=2;
		String stateName= "Test Update State";
		State state = repo.findById(stateId).get();
		
		state.setName(stateName);		
		State updateState = repo.save(state);
		
		assertThat(updateState.getName()).isEqualTo(stateName);
	}
	
	@Test
	public void testGetState() {
		Integer stateId =2;
		Optional<State> findById = repo.findById(stateId);
		
		assertThat(findById.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId =1;
		repo.deleteById(stateId);
		
		Optional<State> findById = repo.findById(stateId);
		
		assertThat(findById).isEmpty();
	}
}


