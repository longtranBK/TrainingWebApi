package com.example.demo.constant;

import lombok.NonNull;

public enum StatusFriendEnum {
	
	REQUEST("0", "SENDED_REQUEST"), 
	FRIEND("1", "FRIEND"),
	NOT_FRIEND("2", "NOT_FRIEND");
	

	private String value;
	private String label;

	private StatusFriendEnum(String value, String label) {
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
	public static StatusFriendEnum fromValue(@NonNull String value) {
		for (StatusFriendEnum result : StatusFriendEnum.values()) {
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
	public static StatusFriendEnum fromLabel(@NonNull String label) {
		for (StatusFriendEnum result : StatusFriendEnum.values()) {
			if (result.getLabel().equals(label)) {
				return result;
			}
		}
		return null;
	}
}
