package com.org.coop.bs.mapper.converter;

import org.apache.commons.lang3.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.coop.bs.util.AdminSvcCommonUtil;
import com.org.coop.canonical.beans.BranchRuleBean;
import com.org.coop.canonical.beans.UserMasterBean;
import com.org.coop.society.data.admin.entities.BranchMaster;
import com.org.coop.society.data.admin.entities.BranchRule;
import com.org.coop.society.data.admin.entities.RuleMaster;
import com.org.coop.society.data.admin.entities.User;
import com.org.coop.society.data.admin.entities.UserCredential;
import com.org.coop.society.data.admin.entities.UserCredentialOtp;
import com.org.coop.society.data.admin.repositories.BranchMasterRepository;
import com.org.coop.society.data.admin.repositories.RuleMasterRepository;

@Component("branchUsersCC")
public class BranchUsersCC extends DozerConverter<User, UserMasterBean> implements MapperAware, CustomConverter {
	
	@Autowired
	private BranchMasterRepository branchMasterRepository;
	
	@Autowired
	private AdminSvcCommonUtil adminSvcCommonUtil;
	
	public BranchUsersCC() {
		super(User.class, UserMasterBean.class);
	}
	
	public BranchUsersCC(Class prototypeA, Class prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	public void setMapper(Mapper arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User convertFrom(UserMasterBean src, User dest) {
		if(src != null) {
				BranchMaster branch = branchMasterRepository.findOne(src.getBranchId());
				if(branch != null) {
					dest.setBranchMaster(branch);
					UserCredential userCred = dest.getUserCredential();
					if(dest.getUserCredential() == null) {
						userCred = new UserCredential();
					}
					
					userCred.setUserId(src.getUserId());
					userCred.setUser(dest);
					if(StringUtils.isNotBlank(src.getPassword()) && !adminSvcCommonUtil.isPasswordMatched(src.getPassword(), userCred.getPassword())) {
						userCred.setPassword(adminSvcCommonUtil.getEncodedPassword(src.getPassword()));
					}
					if(StringUtils.isNotBlank(src.getTransactionPassword()) && !adminSvcCommonUtil.isPasswordMatched(src.getTransactionPassword(), userCred.getTransactionPwd())) {
						userCred.setTransactionPwd(adminSvcCommonUtil.getEncodedPassword(src.getTransactionPassword()));
					}
					userCred.setCreateUser(src.getCreateUser());
					dest.setUserCredential(userCred);
				}
			}
		return dest;
	}

	@Override
	public UserMasterBean convertTo(User src, UserMasterBean dest) {
		if(src != null) {
			dest.setBranchId(src.getBranchMaster().getBranchId());
			dest.setPassword(src.getUserCredential().getCreateUser());
			dest.setTransactionPassword(src.getUserCredential().getTransactionPwd());
		}
		return dest;
	}
}