package com.inn.test.restimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.test.POJO.Category;
import com.inn.test.constents.CafeConstents;
import com.inn.test.rest.CategoryRest;
import com.inn.test.services.CategoryService;
import com.inn.test.utils.CafeUtills;


@RestController
public class CategoryRestImpl implements CategoryRest {

	@Autowired
	CategoryService categoryService;
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			return categoryService.addNewCategory(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
	}
	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			return categoryService.updateCategory(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
