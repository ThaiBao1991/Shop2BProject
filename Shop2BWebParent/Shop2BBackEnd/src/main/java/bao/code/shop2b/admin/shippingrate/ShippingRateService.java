package bao.code.shop2b.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bao.code.shop2b.admin.country.CountryRepository;
import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.common.entity.Country;
import bao.code.shop2b.common.entity.ShippingRate;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE =4;
	
	@Autowired private ShippingRateRepository shipRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ShippingRateRepository shippingRateRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	
	public List<ShippingRate> listAllShippingRates (){
		return (List<ShippingRate>) shippingRateRepo.findAll();
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExitsException{
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(),
				rateInForm.getState());
		
		boolean foundExistingRateInNewMode = rateInForm.getId() ==  null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB  != null;
		
		if(foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExitsException("There's already a rate for the destination is "
					+ rateInForm.getCountry().getName() + "," + rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException{
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID =" +id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported ) throws ShippingRateNotFoundException{
		Long count = shipRepo.countById(id);
		if(count== null || count ==0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID =" +id);
		}
		shipRepo.updateCODSupport(id, codSupported);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException{
		Long count = shipRepo.countById(id);
		if(count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID =" +id);
		}
		shipRepo.deleteById(id);
	}
}
