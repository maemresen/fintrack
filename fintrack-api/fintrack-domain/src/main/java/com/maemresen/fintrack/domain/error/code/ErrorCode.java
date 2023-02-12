package com.maemresen.fintrack.domain.error.code;

public interface ErrorCode {

	String getCodePrefix();

	int getCodeNumber();

	default String getCode(){
		return String.format("%s%d", getCodePrefix(), getCodeNumber());
	}
}
