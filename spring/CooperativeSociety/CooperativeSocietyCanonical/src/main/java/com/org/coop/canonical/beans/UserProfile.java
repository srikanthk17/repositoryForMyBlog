package com.org.coop.canonical.beans;

import java.util.Map;

public class UserProfile {
	private String userName;
	private String password;
	private String otp;
	private boolean isOtpMatch;
	private boolean isOtpEnabled;
	private Map<String, String> branchRuleMap;
	
	public Map<String, String> getBranchRuleMap() {
		return branchRuleMap;
	}
	public void setBranchRuleMap(Map<String, String> branchRuleMap) {
		this.branchRuleMap = branchRuleMap;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public boolean isOtpMatch() {
		return isOtpMatch;
	}
	public void setOtpMatch(boolean isOtpMatch) {
		this.isOtpMatch = isOtpMatch;
	}
	public boolean isOtpEnabled() {
		return isOtpEnabled;
	}
	public void setOtpEnabled(boolean isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
	}
	
}
