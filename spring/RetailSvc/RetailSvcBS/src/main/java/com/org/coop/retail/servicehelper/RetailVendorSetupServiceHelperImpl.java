package com.org.coop.retail.servicehelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coop.org.exception.AdminSvcCommonException;
import com.org.coop.bs.util.CommonValidationUtils;
import com.org.coop.bs.util.RetailBusinessConstants;
import com.org.coop.canonical.beans.AccountBean;
import com.org.coop.canonical.beans.BranchBean;
import com.org.coop.canonical.beans.CustomerBean;
import com.org.coop.canonical.beans.UIModel;
import com.org.coop.canonical.retail.beans.RetailVendorBean;
import com.org.coop.retail.bs.mapper.RetailVendorMappingImpl;
import com.org.coop.retail.entities.Account;
import com.org.coop.retail.entities.BranchMaster;
import com.org.coop.retail.entities.Customer;
import com.org.coop.retail.entities.VendorMaster;
import com.org.coop.retail.repositories.AccountRepository;
import com.org.coop.retail.repositories.RetailBranchMasterRepository;
import com.org.coop.retail.repositories.RetailVendorMasterRepository;

@Service
public class RetailVendorSetupServiceHelperImpl {

	private static final Logger log = Logger.getLogger(RetailVendorSetupServiceHelperImpl.class); 
	
	@Autowired
	private RetailBranchMasterRepository branchMasterRepository;
	
	@Autowired
	private RetailVendorMappingImpl retailVendorMappingImpl;
	
	@Autowired
	private RetailVendorMasterRepository retailVendorMasterRepository;
	
	@Autowired
	private CustomerSetupServiceHelperImpl customerSetupServiceHelperImpl;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CommonValidationUtils commonValidationUtils;
	
	@Transactional(value="retailTransactionManager")
	public UIModel getVendor(int vendorId) {
		UIModel uiModel = new UIModel();
		// Check if the Vendor already exists
		VendorMaster vendor = retailVendorMasterRepository.findOne(vendorId);
		if(vendor == null) {
			uiModel.setErrorMsg("Vendor does not exist for vendor Id: " + vendorId);
			return uiModel;
		}
		BranchBean branchBean = new BranchBean();
		RetailVendorBean vendorBean = new RetailVendorBean();
		List<RetailVendorBean> vendorBeanList = new ArrayList<RetailVendorBean>();
		vendorBeanList.add(vendorBean);
		branchBean.setRetailVendors(vendorBeanList);
		
		retailVendorMappingImpl.mapBean(vendor, vendorBean);
		uiModel.setBranchBean(branchBean);
		branchBean.setBranchId(vendor.getBranchMaster().getBranchId());
		
		if(log.isDebugEnabled()) {
			log.debug("Vendor details has been retrieved from database for vendorId: " + vendorId);
		}
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel saveVendor(UIModel uiModel) {
		if(uiModel.getBranchBean().getRetailVendors() != null && uiModel.getBranchBean().getRetailVendors().size() > 0 ) {
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			int vendorId = vendorBean.getVendorId();
			VendorMaster vendor = null;
			int accountId = 0;
			
			if(vendorBean.getVendorName() != null) {
				vendorBean.setVendorName(vendorBean.getVendorName().trim().toUpperCase());
			}
			
			if(vendorId == 0) {
				vendor = new VendorMaster();
				BranchMaster branchMaster = commonValidationUtils.validateBranch(vendorBean.getBranchId());
				
				// Attach vendor with the parent branch only
				if(branchMaster.getParentId() > 0) {
					branchMaster = commonValidationUtils.validateBranch(branchMaster.getParentId());
				}
				vendor.setBranchMaster(branchMaster);
				
				if(log.isDebugEnabled()) {
					log.debug("Adding new vendor");
				}
				
				// *******************************************
				// Add an account automatically for the vendor.
				// Also add vendor as a customer automatically.
				// *******************************************
				AccountBean accountBean = new AccountBean();
				accountBean.setAccountId(0);
				accountBean.setAccountType(RetailBusinessConstants.TYPE_OF_ACCOUNT_CRDR);
				accountBean.setBranchId(vendorBean.getBranchId());
				accountBean.setActionDate(new Date());
				accountBean.setCreateUser(vendorBean.getCreateUser());
				uiModel.getBranchBean().setAccounts(new ArrayList<AccountBean>());
				uiModel.getBranchBean().getAccounts().add(accountBean);
				
				CustomerBean customerBean = new CustomerBean();
				customerBean.setCustomerId(0);
				customerBean.setBranchId(vendorBean.getBranchId());
				customerBean.setFirstName(vendorBean.getVendorName());
				String vendorType = (vendorBean.getVendorType() == null) ? "VENDOR" : vendorBean.getVendorType();
				customerBean.setSalute(vendorType);
				customerBean.setCustomerType(vendorType);
				customerBean.setMobile1(vendorBean.getPhone1());
				customerBean.setCreateUser(vendorBean.getCreateUser());
				customerBean.setActionDate(new Date());
				
				uiModel.getBranchBean().setCustomers(new ArrayList<CustomerBean>());
				uiModel.getBranchBean().getCustomers().add(customerBean);
				customerSetupServiceHelperImpl.saveCustomerAccounts(uiModel);
				accountId = uiModel.getBranchBean().getAccounts().get(0).getAccountId();
				
			} else {
				vendor = retailVendorMasterRepository.findOne(vendorId);
				if(vendor == null) {
					String errorMsg = "Vendor Id " + vendorId + " does not exists in our record.";
					log.error(errorMsg);
					throw new AdminSvcCommonException(errorMsg, RetailBusinessConstants.EXCEPTION_RETAIL_VENDOR_MGMT);
				}
				if(log.isDebugEnabled()) {
					log.debug("Modifying existing Vendor for vendorId = " + vendorId);
				}
				Customer cust = vendor.getAccount().getCustomerAccounts().get(0).getCustomer();
				cust.setFirstName(vendorBean.getVendorName());
				cust.setMobile1(vendorBean.getPhone1());
				cust.setUpdateUser(vendorBean.getUpdateUser());
			}
			
			retailVendorMappingImpl.mapBean(vendorBean, vendor);
			
			if(accountId > 0) {
				Account account = accountRepository.findOne(accountId);
				if(account == null) {
					String errorMsg = "Account Id: " + accountId + " does not exists in our records";
					log.error(errorMsg);
					throw new AdminSvcCommonException(errorMsg, RetailBusinessConstants.EXCEPTION_RETAIL_VENDOR_MGMT);
				}
				vendor.setAccount(account);
			}
			retailVendorMasterRepository.saveAndFlush(vendor);
			vendorBean.setVendorId(vendor.getVendorId());
		} else {
			uiModel.setErrorMsg("Vendor details not passed correctly");
		}
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel deleteVendor(UIModel uiModel) {
		if(uiModel.getBranchBean().getRetailVendors() != null && uiModel.getBranchBean().getRetailVendors().size() > 0 ) {
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			int vendorId = vendorBean.getVendorId();
			VendorMaster vendor = retailVendorMasterRepository.findOne(vendorId);
			if(vendor == null) {
				log.error("Vendor does not exists for vendorId: " + vendorId);
				uiModel.setErrorMsg("Vendor does not exists for vendorId: " + vendorId);
				return uiModel;
			}
			
			try {
				if(retailVendorMasterRepository.checkIfAnyChildRecordExists(vendorId) > 0) {
					log.error("Vendor is in use for vendorId = " + vendorId);
					uiModel.setErrorMsg("Vendor is in use for vendorId = " + vendorId);
					return uiModel;
				} else {
					retailVendorMappingImpl.mapBean(vendorBean, vendor);
					retailVendorMasterRepository.saveAndFlush(vendor);
					retailVendorMasterRepository.delete(vendor);
				}
			} catch (Exception e) {
				log.error("Unable to delete vendor for vendorId = " + vendorId);
				uiModel.setErrorMsg("Unable to delete vendor for vendorId = " + vendorId);
				return uiModel;
			}
			if(log.isDebugEnabled()) {
				log.debug("Vendor deleted for vendorId = " + vendorId);
			}
		} else {
			uiModel.setErrorMsg("Vendor can not be deleted because the details has not passed correctly");
		}
		return uiModel;
	}
	
}
