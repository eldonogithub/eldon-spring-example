package com.example.demo;

import org.springframework.http.HttpStatus;

public class Data {

	private int status;
	private String reason;
	private int number;
	
	public void setStatus(HttpStatus status) {
		this.status = status.value();
		this.reason = status.getReasonPhrase();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Data(int number) {
		super();
		this.number = number;
	}
}
