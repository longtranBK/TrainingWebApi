package com.example.demo.dto.response;

public interface UserInforResDto {
	
	String getUserId();

	String getFullname();

	String getAvatarUrl();

	String getSex();
	
	void setSex(String sex);

	String getStudyAt();

	String getWorkingAt();

	String getFavorites();

	String getOtherInfor();

	String getDateOfBirth();

}
