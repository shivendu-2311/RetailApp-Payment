package com.inn.test.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.test.POJO.Bill;

public interface BillService {

	ResponseEntity<String> generateReport(Map<String, Object> requestMap);

	ResponseEntity<List<Bill>> getBills();

	ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

	ResponseEntity<String> deleteBill(Integer id);

	
}
