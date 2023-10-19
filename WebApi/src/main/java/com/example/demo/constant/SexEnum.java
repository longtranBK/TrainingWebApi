package com.example.demo.constant;

import lombok.NonNull;

public enum SexEnum {
	
	MALE("0", "MALE"), 
	FEMALE("1", "FEMALE");

	private String value;
	private String label;

	private SexEnum(String value, String label) {
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
	public static SexEnum fromValue(@NonNull String value) {
		for (SexEnum result : SexEnum.values()) {
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
	public static SexEnum fromLabel(@NonNull String label) {
		for (SexEnum result : SexEnum.values()) {
			if (result.getLabel().equals(label)) {
				return result;
			}
		}
		return null;
	}
}
