package com.prosnav.report.core;

public class ReportException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3993374511240594358L;

	public ReportException(String msg, Exception e) {
		super(msg, e);
	}
}
