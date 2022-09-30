package bao.code.shop2b.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bao.code.shop2b.admin.paging.PagingAndSortingHelper;
import bao.code.shop2b.common.entity.Brand;
import bao.code.shop2b.common.exception.BrandNotFoundException;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE =4;
	
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll (){
		return (List<Brand>) repo.findAll();
	}
	
	public void listByPage(int pageNum ,PagingAndSortingHelper helper){
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException{
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with ID = " +id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException{
		Long countByid = repo.countById(id);
		
		if(countByid==null || countByid==0) {
			throw new BrandNotFoundException("Could not find any brand with ID = " +id);
		}
		repo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew =(id==null || id==0);
		Brand brandByName = repo.findByName(name);
		
		if(isCreatingNew) {
			if(brandByName!=null) return "Duplicate";
		}else {
			if(brandByName!=null && brandByName.getId()!=id) {
				return "Duplicate";
			}
		}
		return "OK";
	}
}

	