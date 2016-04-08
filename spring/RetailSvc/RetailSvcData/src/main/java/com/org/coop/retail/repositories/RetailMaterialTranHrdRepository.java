package com.org.coop.retail.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.org.coop.retail.entities.MaterialTranHrd;

public interface RetailMaterialTranHrdRepository extends JpaRepository<MaterialTranHrd, Integer> {
	
	@Query(value="SELECT CONCAT(DATE_FORMAT(CURDATE(), '%d%m%y'), '/', COUNT(*)+1) AS TRAN_ID "
			+ "FROM material_tran_hrd WHERE "
			+ "DATE(`create_date`) = CURDATE()", nativeQuery=true)
	public String getTransactionNumber();
}
