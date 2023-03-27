package com.inn.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.inn.test.POJO.Product;
import com.inn.test.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

	List<ProductWrapper>getAllProduct();

	@Modifying
	@Transactional//if we not put these two annotaaaion our table will not be updated
	Integer updateProductStatus(@Param("status") String status,@Param("id") Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

	ProductWrapper getProductById(@Param("id")Integer id);

}