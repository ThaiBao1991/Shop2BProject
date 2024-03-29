package bao.code.shop2b.admin.shippingrate;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import bao.code.shop2b.admin.paging.SearchRepository;
import bao.code.shop2b.common.entity.ShippingRate;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer>{
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state = ?2")
	public ShippingRate findByCountryAndState(Integer countryId, String state);
	
	@Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
	@Modifying
	public void updateCODSupport(Integer id, boolean enabled);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE CONCAT(sr.country.name , ' ' , sr.state) LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
}
