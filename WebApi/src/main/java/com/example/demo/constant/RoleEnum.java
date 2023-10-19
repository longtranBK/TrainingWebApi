package com.example.demo.constant;

import lombok.NonNull;

public enum RoleEnum {
	USER("1", "ROLE_USER"), 
	ADMIN("2", "ROLE_ADMIN");

	private String value;
	private String label;

	private RoleEnum(String value, String label) {
	    this.value = value;
	    this.label = label;
	  }

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Get label from value
	 * 
	 * @param value
	 * @return value
	 */
	public static RoleEnum fromValue(@NonNull String value) {
		for (RoleEnum result : RoleEnum.values()) {
			if (result.getValue().equals(value)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Get value from label
	 * 
	 * @param label
	 * @return label
	 */
	public static RoleEnum fromLabel(@NonNull String label) {
		for (RoleEnum result : RoleEnum.values()) {
			if (result.getLabel().equals(label)) {
				return result;
			}
		}
		return null;
	}
}
