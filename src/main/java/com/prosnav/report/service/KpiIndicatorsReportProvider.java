package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prosnav.report.batch.KpiIndicatorsStatistics;
import com.prosnav.report.core.BaseParamModel;
import com.prosnav.report.core.IReportDataSourceProvider;
import com.prosnav.report.domain.db.report.KpiIndicators;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.db.upm.WorkGroup;
import com.prosnav.report.domain.db.upm.WorkGroup.Worker;

@Service
public class KpiIndicatorsReportProvider extends AbstractProvider<BaseParamModel>{

	@Override
	public Class<BaseParamModel> getParamModelType() {
		return BaseParamModel.class;
	}
	
	@Autowired
	@Qualifier("mongoTemplateReport")
	private MongoTemplate mongoReport;
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	
	@Autowired
	private KpiIndicatorsStatistics kpiIndicators;
	
	@Override
	public JRDataSource getReportDataSource(BaseParamModel paramModel) {
//		kpiIndicators.exec();
		String beginDate = paramModel.getSelectionBeginDateISO();
		String endDate = paramModel.getSelectionEndDateISO();
		
		if(StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate)) {
			KpiIndicators model = new KpiIndicators();
			model.setGroupName("");
			model.setUserRealName("");
			return new JRBeanCollectionDataSource(new ArrayList<KpiIndicators>(Arrays.asList(model)));
		}
//		if((beginDateStr == null || beginDateStr.length == 0) && (endDateStr == null || endDateStr.length == 0)) {
//			KpiIndicators model = new KpiIndicators();
//			model.setGroupName("");
//			model.setUserRealName("");
//			return new JRBeanCollectionDataSource(new ArrayList<KpiIndicators>(Arrays.asList(model)));
//		}
		
//		Date beginDate = DateUtil.getCurrDayFirst(Integer.parseInt(beginDateStr[0]), Integer.parseInt(beginDateStr[1]), Integer.parseInt(beginDateStr[2])+1);
//		Date endDate = DateUtil.getCurrDayFirst(Integer.parseInt(endDateStr[0]), Integer.parseInt(endDateStr[1]), Integer.parseInt(endDateStr[2])+1);
		
		StringBuilder matchStr = new StringBuilder();
		StringBuilder groupStr = new StringBuilder();
		
		matchStr.append("{$match: {")
		.append("ctime: {$gte: {$date: '" +beginDate+ "'}, $lte: {$date: '"+ endDate +"'}}")
		.append("}}");

		groupStr.append("{$group: {")
				.append("_id: {userId: '$userId'},")
				.append("potentialCount: {$sum: '$potentialCount'},")
				.append("connectedCount: {$sum: '$connectedCount'},")
				.append("proxyConnectedCount: {$sum: '$proxyConnectedCount'},")
				.append("noResponseCount: {$sum: '$noResponseCount'},")
				.append("closedCount: {$sum: '$closedCount'},")
				.append("busySignalCount: {$sum: '$busySignalCount'},")
				.append("refusedCount: {$sum: '$refusedCount'},")
				.append("vacantCount: {$sum: '$vacantCount'},")
				.append("faxCount: {$sum: '$faxCount'},")
				.append("reAssignCount: {$sum: '$reAssignCount'},")
				.append("bc20Count: {$sum: '$bc20Count'},")
				.append("bc40Count: {$sum: '$bc40Count'},")
				.append("bc60Count: {$sum: '$bc60Count'},")
				.append("bc80Count: {$sum: '$bc80Count'},")
				.append("dealCount: {$sum: '$dealCount'},")
				.append("vipCount: {$sum: '$vipCount'},")
				.append("diamondVipCount: {$sum: '$diamondVipCount'},")
				.append("crownedVipCount: {$sum: '$crownedVipCount'}")
				.append("}}");
		List<AggregationOperation> opList = new ArrayList<AggregationOperation>();
		
		final DBObject matcher = (DBObject) JSON.parse(matchStr.toString());
		final DBObject grouper = (DBObject) JSON.parse(groupStr.toString());
		
		opList.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(matcher);
			}
		});
		
		opList.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(grouper);
			}
		});
		
		TypedAggregation<KpiIndicators> kpiIndicatorsAggregation = Aggregation.newAggregation(KpiIndicators.class, opList);
		AggregationResults<KpiIndicators> kpiIndicatorsAggregationResult = mongoReport.aggregate(kpiIndicatorsAggregation, "kpiIndicators", KpiIndicators.class);
		List<KpiIndicators> kpiIndicatorsList = kpiIndicatorsAggregationResult.getMappedResults();
		
		Map<Long, WorkGroup> userGroupMap = new HashMap<Long, WorkGroup>();
		Map<Long, User> userMap = new HashMap<Long, User>();
		
		List<WorkGroup> workGroupList = mongoUpm.find(new Query(Criteria.where("isDel").ne(true)), WorkGroup.class);
		for(WorkGroup workGroup: workGroupList) {
			for(Worker worker: workGroup.getWorkers()) {
				userGroupMap.put(worker.getUserid(), workGroup);
			}
		}
		
		List<User> userList = mongoUpm.find(new Query(Criteria.where("_id").in(userGroupMap.keySet())), User.class);
		for(User user: userList) {
			userMap.put(user.get_id(), user);
		}
		
		Map<Long, List<KpiIndicators>> kpiIndicatorsMap = new HashMap<Long, List<KpiIndicators>>();
		for(KpiIndicators kpiIndicators: kpiIndicatorsList) {
			WorkGroup workGroup = userGroupMap.get(kpiIndicators.getUserId());
			User user = userMap.get(kpiIndicators.getUserId());
			
			if(workGroup != null) {
				kpiIndicators.setGroupId(workGroup.get_id());
				kpiIndicators.setGroupName(workGroup.getName());
			}
			
			if(user != null) {
				kpiIndicators.setUsername(user.getUsername());
				kpiIndicators.setUserRealName(user.getRealName());
			}
			
			if(workGroup != null) {
				if(kpiIndicatorsMap.containsKey(workGroup.get_id())) {
					kpiIndicatorsMap.get(workGroup.get_id()).add(kpiIndicators);
				}else {
					List<KpiIndicators> kpiList = new ArrayList<KpiIndicators>();
					kpiList.add(kpiIndicators);
					kpiIndicatorsMap.put(workGroup.get_id(), kpiList);
				}
			}
		}
		
		List<KpiIndicators> kpiIndicatorsResultList = new ArrayList<KpiIndicators>();
		
		for(Entry<Long, List<KpiIndicators>> kpiIndicators: kpiIndicatorsMap.entrySet()) {
			kpiIndicatorsResultList.addAll(kpiIndicators.getValue());
		}

		
//		Collections.sort(kpiIndicatorsList, new Comparator<KpiIndicators>() {
//	        @Override
//	        public int compare(KpiIndicators kpi1, KpiIndicators kpi2)
//	        {
//
//	            return  String.valueOf(kpi1.getGroupId()).compareTo(String.valueOf(kpi2.getGroupId()));
//	        }
//	    });
		
//		Criteria criteria;
//		if((beginDate == null || beginDate.length == 0) && (endDate == null || endDate.length == 0)) {
//			KpiIndicators model = new KpiIndicators();
//			model.setGroupName("");
//			model.setUserRealName("");
//			return new JRBeanCollectionDataSource(new ArrayList<KpiIndicators>(Arrays.asList(model)));
//		}else if((beginDate == null || beginDate.length == 0) && (endDate != null && endDate.length != 0)){
//			criteria = Criteria.where("ctime")
//					.lte(DateUtil.getCurrDayLast(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2])+1));
//		}else if((beginDate != null && beginDate.length != 0) && (endDate == null || endDate.length == 0)) {
//			criteria = Criteria.where("ctime")
//					.gte(DateUtil.getCurrDayFirst(Integer.parseInt(beginDate[0]), Integer.parseInt(beginDate[1]), Integer.parseInt(beginDate[2])+1));
//		}else {
//			criteria = Criteria.where("ctime")
//					.gte(DateUtil.getCurrDayFirst(Integer.parseInt(beginDate[0]), Integer.parseInt(beginDate[1]), Integer.parseInt(beginDate[2])+1))
//					.lte(DateUtil.getCurrDayLast(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2])+1));
//		}
//		List<KpiIndicators> kpiIndicatorsList = mongoReport.find(new Query().with(new Sort("groupId")), KpiIndicators.class);
		return new JRBeanCollectionDataSource(kpiIndicatorsResultList);
	}
	
	@Override
	public com.prosnav.report.core.IReportDataSourceProvider.DateType getInitDateType() {
		return IReportDataSourceProvider.DateType.DAY;
	}
}
