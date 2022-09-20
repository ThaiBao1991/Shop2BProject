package bao.code.shop2b.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bao.code.shop2b.common.entity.Category;

@Service
@Transactional
public class CategoryService {

	
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories(){
		List<Category> listNoChildrenCategories = new ArrayList<Category>();
		
		List<Category> listEnabledCategories = repo.findAllEnabled();
		
		listEnabledCategories.forEach(category ->{
			Set<Category> children =category.getChildren();
			if(children == null || children.size() ==0) {
				listNoChildrenCategories.add(category);
			}
		});
		return listNoChildrenCategories;
	}

}

	