package com.org.coop.admin.ws;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.coop.admin.service.BranchSetupServiceImpl;
import com.org.coop.admin.service.MasterDataSetupServiceImpl;
import com.org.coop.canonical.beans.UIModel;
import com.org.coop.canonical.master.beans.CountryMasterBean;
import com.org.coop.canonical.master.beans.MasterDataBean;
import com.org.coop.canonical.master.beans.ModuleMasterBean;

@RestController
@RequestMapping("/rest")
public class MasterDataSetupWSImpl {
	
	private static final Logger log = Logger.getLogger(MasterDataSetupWSImpl.class); 
	
	@Autowired
	private MasterDataSetupServiceImpl masterDataSetupServiceImpl;
	
	/**
	 * Country/State/District can be saved together or individually
	 * @param masterDataBean
	 * @return
	 */
	@RequestMapping(value = "/saveCountryStateDist", method = RequestMethod.POST, headers="Accept=application/json",produces="application/json",consumes="application/json")
	public MasterDataBean saveCountryStateDist(@RequestBody MasterDataBean masterDataBean) {
		try {
			masterDataBean = masterDataSetupServiceImpl.saveCountryStateDist(masterDataBean);
		} catch (Exception e) {
			log.error("Error Creating/Updating country/state/district", e);
			masterDataBean.setErrorMsg("Error Creating/Updating country/state/district");
		}
		return masterDataBean;
	}
	
	@RequestMapping(value = "/getCountryStateDist", method = RequestMethod.GET, headers="Accept=application/json",produces="application/json")
	public MasterDataBean getCountryStateDist(@RequestParam(value = "countryCode",required = true,defaultValue = "") String countryCode,
			@RequestParam(value = "stateCode",required = true,defaultValue = "") String stateCode,
			@RequestParam(value = "distCode",required = true,defaultValue = "") String distCode) {
		MasterDataBean masterData = new MasterDataBean();
		try {
			masterData = masterDataSetupServiceImpl.getCountryStateDist(countryCode,stateCode,distCode);
		} catch (Exception e) {
			log.error("Error Retrieving Country by Country Id", e);
			masterData.setErrorMsg("Error Retrieving Country by Country Id: " + countryCode);
		}
		return masterData;
	}
	
	@RequestMapping(value = "/getModuleRulesAndPermissions", method = RequestMethod.GET, headers="Accept=application/json",produces="application/json")
	public MasterDataBean getModuleRulesAndPermissions(@RequestParam(value = "moduleName",required = true,defaultValue = "") String moduleName) {
		MasterDataBean masterData = new MasterDataBean();
		try {
			masterData = masterDataSetupServiceImpl.getModuleRulesAndPermissions(moduleName);
		} catch (Exception e) {
			log.error("Error Retrieving Module details for module Name", e);
			masterData.setErrorMsg("Error Retrieving Module details for module Name: " + moduleName);
		}
		return masterData;
	}
	
	@RequestMapping(value = "/saveModuleRulesAndPermissions", method = RequestMethod.POST, headers="Accept=application/json",produces="application/json")
	public MasterDataBean saveModuleRulesAndPermissions(@RequestBody MasterDataBean masterDataBean) {
		MasterDataBean masterData = new MasterDataBean();
		try {
			masterData = masterDataSetupServiceImpl.saveModuleRulesAndPermissions(masterDataBean);
		} catch (Exception e) {
			log.error("Error Retrieving Module details for module:" + ((ModuleMasterBean)masterDataBean.getModules().toArray()[0]).getModuleName() , e);
			masterData.setErrorMsg("Error saving Module details for module Name: " + ((ModuleMasterBean)masterDataBean.getModules().toArray()[0]).getModuleName());
		}
		return masterData;
	}
	
	@RequestMapping(value = "/getSecurityQuestions", method = RequestMethod.GET, headers="Accept=application/json",produces="application/json")
	public MasterDataBean getSecurityQuestions(@RequestParam(value = "noOfSecurityQuestion",required = true,defaultValue = "0") int noOfSecurityQuestion) {
		MasterDataBean masterData = new MasterDataBean();
		try {
			masterData = masterDataSetupServiceImpl.getSecurityQuestions(noOfSecurityQuestion);
		} catch (Exception e) {
			log.error("Error Retrieving Security questions:" , e);
			masterData.setErrorMsg("Error Retrieving the Security questions");
		}
		return masterData;
	}
	
}