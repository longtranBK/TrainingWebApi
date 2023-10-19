package com.example.demo.constant;

import lombok.NonNull;

public enum StatusPostEnum {
	
	PUBLIC("0", "PUBLIC"), 
	PRIVATE("1", "PRIVATE");

	private String value;
	private String label;

	private StatusPostEnum(String value, String label) {
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
	public static StatusPostEnum fromValue(@NonNull String value) {
		for (StatusPostEnum result : StatusPostEnum.values()) {
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
	public static StatusPostEnum fromLabel(@NonNull String label) {
		for (StatusPostEnum result : StatusPostEnum.values()) {
			if (result.getLabel().equals(label)) {
				return result;
			}
		}
		return null;
	}
}
