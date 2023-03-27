package com.inn.test.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.test.dao.BillDao;
import com.inn.test.dao.CategoryDao;
import com.inn.test.dao.ProductDao;
import com.inn.test.dao.UserDao;

@Service
public class DashboardServiceImpl implements DashboardService  {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	BillDao billDao;
	
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		Map<String, Object>map = new HashMap<>();
		map.put("category", categoryDao.count());
		map.put("product", productDao.count());
		map.put("bill", billDao.count());
		return new ResponseEntity(map, HttpStatus.OK);
	}

}
