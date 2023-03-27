package com.inn.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.google.common.base.Strings;
import com.inn.test.JWT.JwtFilter;
import com.inn.test.POJO.Category;
import com.inn.test.constents.CafeConstents;
import com.inn.test.dao.CategoryDao;
import com.inn.test.utils.CafeUtills;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryDao categoryDao;//because we have to save data
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
	if(jwtFilter.isAdmin())
	{
		if(validateCategoryMap(requestMap, false))
		{
			categoryDao.save( getCategoryFromMap(requestMap,false));
			return CafeUtills.getResponseEntity("Category updated SucessFully", HttpStatus.OK);
		}
	}
	else
	{
		return CafeUtills.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
	}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		
		if(requestMap.containsKey("name"))
		{
			if(requestMap.containsKey("id") && validateId)
			{
				return true;
			}
			else if(!validateId)
			{
				return true;
			}
		}
			return false;
	}
	
	private Category getCategoryFromMap(Map<String, String>requestMap, boolean isAdd)
	{
		Category category = new Category();
		if(isAdd)
		{
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true"))
			{
				System.out.println("InsideIF");
			return  new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
			}
			
			return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
		}
		catch(Exception ex){
ex.printStackTrace();			
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin())
			{
				if(validateCategoryMap(requestMap, true))
				{
					java.util.Optional<Category> optional =categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if(!optional.isEmpty())
					{
						categoryDao.save(getCategoryFromMap(requestMap,true));
						return CafeUtills.getResponseEntity("CategoryAddedSucesfully", HttpStatus.OK);
					}
					else
					{
						return CafeUtills.getResponseEntity("Category ID Doesn't Exist", HttpStatus.OK);
					}
					
							
				}
			  return CafeUtills.getResponseEntity("Data is not valid", HttpStatus.BAD_REQUEST);
			
			}
			else
			{
				return CafeUtills.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
				
	}

	

}
