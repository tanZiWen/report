/**
 * 
 */
package com.prosnav.report.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prosnav.report.batch.WorkGroupBusinessStatusStatistics;
import com.prosnav.report.domain.db.report.BaseReportModel;

/**
 * @author wangnan
 *
 */
@Component
public class ReportBatchExecutor {
	
	private final static Logger LOGGER = LogManager.getLogger(WorkGroupBusinessStatusStatistics.class);
	
	@Autowired(required=false)
	private List<IReportBatch<? extends BaseReportModel>> batchList = new ArrayList<IReportBatch<? extends BaseReportModel>>();
	
	@Autowired
	@Qualifier("mongoTemplateReport")
	private MongoTemplate mongoReport;
	
	@Scheduled(cron="1 1 2 * * ?")
	public void executeAll() {
		execute(null);
	}
	
	public void execute(List<String> includes) {
		for(IReportBatch<? extends BaseReportModel> batch : batchList) {
			try {
				Long begin = null;
				if(batch.beforeExec()) {
					if(LOGGER.isDebugEnabled()) {
						begin = new Date().getTime();
						LOGGER.debug("[begin excute] >>> " + batch.getClass().getName());
					}
					if(includes == null || (includes != null && includes.contains(batch.getClass().getSimpleName()))) {
						batch.exec();
					}
				} else {
					if(LOGGER.isDebugEnabled()) {
						begin = new Date().getTime();
						LOGGER.debug("[not excute, data exists] >>> " + batch.getClass().getName());
					}
				}
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("[end excute] >>> " + batch.getClass().getName() + ", use : " + (new Date().getTime() - begin));
				}
			} catch(ReportBatchException e) {
				LOGGER.error("execute report batch failed : " + batch.getClass().getName(), e);
			}
		}
	}
	
	public List<String> getReportBatchNames() {
		List<String> names = new ArrayList<String>();
		for(IReportBatch<? extends BaseReportModel> batch : batchList) {
			names.add(batch.getClass().getSimpleName());
		}
		return names;
	}
}
