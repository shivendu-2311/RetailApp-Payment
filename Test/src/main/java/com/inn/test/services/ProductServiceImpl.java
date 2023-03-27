package com.inn.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.test.JWT.JwtFilter;
import com.inn.test.POJO.Category;
import com.inn.test.POJO.Product;
import com.inn.test.constents.CafeConstents;
import com.inn.test.dao.ProductDao;
import com.inn.test.utils.CafeUtills;
import com.inn.test.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductDao productDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin())
			{
			if(validateProdcutMap(requestMap, false))	//sending false because we want the same on other updates
			{
				productDao.save(getProductFromMap(requestMap, false));
				return CafeUtills.getResponseEntity("Product Added Succesfully", HttpStatus.OK);
			}
			return CafeUtills.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
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

	private boolean validateProdcutMap(Map<String, String> requestMap, boolean validateId) {
		
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
	
	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
		 Category category = new Category();//creating category obj because we have to give refrence to foreign key
		 
		 category.setId(Integer.parseInt(requestMap.get("categoryId")));
		 Product product = new Product();
		 
		 if(isAdd)
		 {
			 product.setId(Integer.parseInt(requestMap.get("id")));
		 }
		 else
		 {
			 product.setStatus("true");
		 }
		 product.setCategory(category);
		 product.setName(requestMap.get("name"));
		 product.setDescription(requestMap.get("description"));
		 product.setPrice(Integer.parseInt(requestMap.get("price")));
		 return product;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin())
			{
		if(validateProdcutMap(requestMap,true))
		{
	java.util.Optional<Product>optional =productDao.findById(Integer.parseInt(requestMap.get("id")));
   if(!optional.isEmpty())
   {
	   Product product = getProductFromMap(requestMap, true);
	   product.setStatus(optional.get().getStatus());
	   productDao.save(product);
	   return CafeUtills.getResponseEntity("Product Updated SucesFUlly",HttpStatus.OK);
   }
   else
   {
	   return CafeUtills.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
   }

		}
		else
		{
			return CafeUtills.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
		}
			}
			else
			{
				return CafeUtills.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);	
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if(jwtFilter.isAdmin())
			{
				java.util.Optional optional = productDao.findById(id);
				if(!optional.isEmpty())
				{
					productDao.deleteById(id);
					 return CafeUtills.getResponseEntity("Product Deleted SucesFUlly",HttpStatus.OK);
					
				}
				else
				{
					  return CafeUtills.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
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

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
	
		try {
			if(jwtFilter.isAdmin())
			{
				java.util.Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				
				if(!optional.isEmpty())
				{
					productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					 return CafeUtills.getResponseEntity("Product Status Updated SucesFUlly",HttpStatus.OK);
				}
				else
				{
					 return CafeUtills.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
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

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
