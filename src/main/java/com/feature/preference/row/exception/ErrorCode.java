package com.feature.preference.row.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
	FPR_0001("Order already exists"),
	FPR_0002("No Order exist"),
	FPR_0003("Order could not be inserted"),
	FPR_0004("Order could not be deleted"),
	FPR_0005("Order could not be retrieved"),
	FPR_0006("Invalid date exception"),
	FPR_0007("Invalid input data exception"),
	FPR_0008("Operation error");

	private String description;

	ErrorCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("error_code")
	public String getName() {
		return name();
	}

	@Override
	public String toString() {
		return getName() + "(" + getDescription() + ")";
	}
}
