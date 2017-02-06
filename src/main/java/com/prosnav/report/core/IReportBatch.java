/**
 * 
 */
package com.prosnav.report.core;


/**
 * @author wangnan
 * 定时任务接口
 */
public interface IReportBatch<T> {
	
	public void exec() throws ReportBatchException;
	public boolean beforeExec();
}
