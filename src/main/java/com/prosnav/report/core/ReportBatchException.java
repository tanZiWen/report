/**
 * 
 */
package com.prosnav.report.core;

/**
 * @author wangnan
 *
 */
public class ReportBatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2142999288771162283L;

	public ReportBatchException() {
		super();
	}
	
	public ReportBatchException(String message, Exception cause) {
		super(message, cause);
	}
}
