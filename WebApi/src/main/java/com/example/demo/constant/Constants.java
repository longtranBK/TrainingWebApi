package com.example.demo.constant;

/**
 * Constant variable
 */
public class Constants {
	
	// Pattern request date input
	public static final String DATE_PATTERN = "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$"
			+ "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
			+ "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
			+ "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$";
	
	public static final String EMAIL_PATTERN = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}";
	
	public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	
	public static final String MESSAGE_REGEX_PASSWORD = "Invalid Password: Password must contain least one digit [0-9]." 
	        + " Password must contain at least one lowercase Latin character [a-z]." 
	        + " Password must contain at least one uppercase Latin character [A-Z]."
	        + " Password must contain at least one special character like ! @ # & ( )."
	        + " Password must contain a length of at least 8 characters and a maximum of 20 characters.";
	
	// Value role admin
	public static final String ROLE_ADMIN_VALUE = "1";
	
	// Value  role user
	public static final String ROLE_USER_VALUE = "2";
	
	// Role name admin
	public static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";
	
	// Role name user
	public static final String ROLE_USER_NAME = "ROLE_USER";
	
	public static final int ACTIVE_USER = 1;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
}
