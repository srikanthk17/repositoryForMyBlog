package com.org.coop.retail.bs.mapper;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.coop.retail.bs.config.RetailDozerConfig;

@Component
public class GlLedgerMappingImpl {
	@Autowired
	private RetailDozerConfig retailDozerConfig;
	
	public void mapGlLedgerHrdBean(Object src, Object dest) {
		Mapper dozerBeanMapper = (Mapper) retailDozerConfig.dozerBean();
		dozerBeanMapper.map(src, dest, "glLedgerHrdMap");
	}
	
	public void mapGlLedgerDtlBean(Object src, Object dest) {
		Mapper dozerBeanMapper = (Mapper) retailDozerConfig.dozerBean();
		dozerBeanMapper.map(src, dest, "glLedgerDtlMap");
	}
	
}