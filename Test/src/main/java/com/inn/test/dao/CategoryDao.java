package com.inn.test.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.test.POJO.Category;

public interface CategoryDao extends JpaRepository<Category,Integer> {
	
	List<Category>getAllCategory();

}
