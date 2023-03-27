package com.inn.test.restimpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.test.POJO.Bill;
import com.inn.test.constents.CafeConstents;
import com.inn.test.rest.BillRest;
import com.inn.test.services.BillService;
import com.inn.test.utils.CafeUtills;

@RestController
public class BillRestImpl implements BillRest{

	@Autowired
	BillService billService;
	
	
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
	try {
		return billService.generateReport(requestMap);
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<List<Bill>> getBills() {
		
		return billService.getBills();
		
		

	}


	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		try {
			return billService.getPdf(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}


	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		try {
			return billService.deleteBill(id);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

}
