package com.inn.test.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inn.test.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer> {

List<Bill> getAllBills();
List<Bill> getBillByUsername(@Param("username") String username);

}
