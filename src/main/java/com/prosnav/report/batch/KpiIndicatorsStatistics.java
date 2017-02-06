package com.prosnav.report.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prosnav.report.core.ReportBatchException;
import com.prosnav.report.domain.db.report.KpiIndicators;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.db.upm.WorkGroup;
import com.prosnav.report.domain.db.upm.WorkGroup.Worker;

@Component
public class KpiIndicatorsStatistics extends AbstractReportBatch<KpiIndicators>{
	
private final static Logger LOGGER = LogManager.getLogger(WorkGroupBusinessStatusStatistics.class);
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoCrm;
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	@Override
	public void exec() throws ReportBatchException {
		List<KpiIndicators> kpiIndicatorsList = new ArrayList<KpiIndicators>();
		Map<Long, KpiIndicators> kpiIndicatorsMap = new HashMap<Long, KpiIndicators>();
		
		Map<Long, WorkGroup> userGroupMap = new HashMap<Long, WorkGroup>();
		Map<Long, User> userMap = new HashMap<Long, User>();
		
		StringBuilder matchUserIdsStr = new StringBuilder();
		StringBuilder matchStr = new StringBuilder();
		StringBuilder projectStr = new StringBuilder();
		StringBuilder groupStr = new StringBuilder();
		
		List<WorkGroup> workGroupList = mongoUpm.find(new Query(Criteria.where("isDel").ne(true)), WorkGroup.class);
		for(WorkGroup workGroup: workGroupList) {
			for(Worker worker: workGroup.getWorkers()) {
				if(matchUserIdsStr.length() == 0) {
					matchUserIdsStr.append(worker.getUserid());
				}else {
					matchUserIdsStr.append(",").append(worker.getUserid());
				}
				userGroupMap.put(worker.getUserid(), workGroup);
			}
		}
		
		List<User> userList = mongoUpm.find(new Query(Criteria.where("_id").in(userGroupMap.keySet())), User.class);
		for(User user: userList) {
			userMap.put(user.get_id(), user);
		}
		
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_MONTH, -2);
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int dayBegin = date.get(Calendar.DAY_OF_MONTH);
		date.add(Calendar.DAY_OF_MONTH, 1);
		int dayEnd = date.get(Calendar.DAY_OF_MONTH);
		String beginDate = year+"-"+month+"-"+dayBegin+"T16:00:01.000Z";
		String endDate = year+"-"+month+"-"+dayEnd+"T15:59:59.000Z";
		
		matchStr.append("{$match: {")
				.append("cuserid: {$in: [").append(matchUserIdsStr).append("]},")
				.append("ctime: {$gte: {$date: '" +beginDate+ "'}, $lte: {$date: '"+ endDate +"'}}")
				.append("}}");
		
		projectStr.append("{$project: {")
				  .append("cuserid: 1,")
				  .append("potential: {$cond: {if: {$eq: ['$customerStatus', 'potential']}, then: 1, else: 0}},")
				  .append("connected: {$cond: {if: {$eq: ['$customerCallStatus', 'connected']}, then: 1, else: 0}},")
				  .append("proxyConnected: {$cond: {if: {$eq: ['$customerCallStatus', 'proxyConnected']}, then: 1, else: 0}},")
				  .append("noResponse: {$cond: {if: {$eq: ['$customerCallStatus', 'noResponse']}, then: 1, else: 0}},")
				  .append("closed: {$cond: {if: {$eq: ['$customerCallStaus', 'closed']}, then: 1, else: 0}},")
				  .append("busySignal: {$cond: {if: {$eq: ['$customerCallStatus', 'busySignal']}, then: 1, else: 0}},")
				  .append("refused: {$cond: {if: {$eq: ['$customerCallStatus', 'refused']}, then: 1, else: 0}},")
				  .append("vacant: {$cond: {if: {$eq: ['$customerCallStatus', 'vacant']}, then: 1, else: 0}},")
				  .append("fax: {$cond: {if: {$eq: ['$customerCallStatus', 'fax']}, then: 1, else: 0}},")
				  .append("reAssign: {$cond: {if: {$eq: ['$customerCallStatus', 'reAssign']}, then: 1, else: 0}}")
				  .append("}}");
		
		groupStr.append("{$group: {")
				.append("_id: {userId: '$cuserid'},")
				.append("potentialCount: {$sum: '$potential'},")
				.append("connectedCount: {$sum: '$connected'},")
				.append("proxyConnectedCount: {$sum: '$proxyConnected'},")
				.append("noResponseCount: {$sum: '$noResponse'},")
				.append("closedCount: {$sum: '$closed'},")
				.append("busySignalCount: {$sum: '$busySignal'},")
				.append("refusedCount: {$sum: '$refused'},")
				.append("vacantCount: {$sum: '$vacant'},")
				.append("faxCount: {$sum: '$fax'},")
				.append("reAssignCount: {$sum: '$reAssign'}")
				.append("}}");
		
		List<AggregationOperation> opList = setAggregationOperation(matchStr,
				projectStr, groupStr);
		
		TypedAggregation<AggregationCustomerServiceRecordModel> serviceAggregation = Aggregation.newAggregation(AggregationCustomerServiceRecordModel.class, opList);
		AggregationResults<AggregationCustomerServiceRecordModel> serviceAggregationResult = mongoCrm.aggregate(serviceAggregation, "crm_customer_service_record", AggregationCustomerServiceRecordModel.class);
		List<AggregationCustomerServiceRecordModel> serviceModelList = serviceAggregationResult.getMappedResults();
		
		Calendar cal = Calendar.getInstance();
		Calendar reportDate = Calendar.getInstance();
		reportDate.add(Calendar.DAY_OF_MONTH, -1);
		for(AggregationCustomerServiceRecordModel model: serviceModelList) {
			WorkGroup workGroup = userGroupMap.get(model.getUserId());
			User user = userMap.get(model.getUserId());
			
			KpiIndicators kpiIndicators = new KpiIndicators();
			kpiIndicators.setReportYear(reportDate.get(Calendar.YEAR));
			kpiIndicators.setReportMonth(reportDate.get(Calendar.MONTH) + 1);
			kpiIndicators.setReportDay(reportDate.get(Calendar.DAY_OF_MONTH));
			kpiIndicators.setGroupId(workGroup.get_id());
			kpiIndicators.setGroupName(workGroup.getName());
			kpiIndicators.setUserId(model.getUserId());
			kpiIndicators.setUsername(user.getUsername());
			kpiIndicators.setUserRealName(user.getRealName());
			kpiIndicators.setPotentialCount(model.getPotentialCount());
			kpiIndicators.setConnectedCount(model.getConnectedCount());
			kpiIndicators.setProxyConnectedCount(model.getProxyConnectedCount());
			kpiIndicators.setNoResponseCount(model.getNoResponseCount());
			kpiIndicators.setClosedCount(model.getClosedCount());
			kpiIndicators.setBusySignalCount(model.getBusySignalCount());
			kpiIndicators.setRefusedCount(model.getRefusedCount());
			kpiIndicators.setVacantCount(model.getVacantCount());
			kpiIndicators.setFaxCount(model.getFaxCount());
			kpiIndicators.setCtime(cal.getTime());
			kpiIndicatorsMap.put(model.getUserId(), kpiIndicators);
			kpiIndicatorsList.add(kpiIndicators);
		}
		
		matchStr.setLength(0);
		matchStr.append("{$match: {")
				.append("'call.callUser.userid': {$in: [").append(matchUserIdsStr).append("]},")
				.append("'call.callTime': {$gte: {$date: '" +beginDate+ "'}, $lte: {$date: '"+ endDate +"'}}")
				.append("}}");
		
		projectStr.setLength(0);
		projectStr.append("{$project: {")
				  .append("'call.callUser.userid': 1,")
				  .append("bc20: {$cond: {if: {$eq: ['$type', 'bc20']}, then: 1, else: 0}},")
				  .append("bc40: {$cond: {if: {$eq: ['$type', 'bc40']}, then: 1, else: 0}},")
				  .append("bc60: {$cond: {if: {$eq: ['$type', 'bc60']}, then: 1, else: 0}},")
				  .append("bc80: {$cond: {if: {$eq: ['$type', 'bc80']}, then: 1, else: 0}},")
				  .append("deal: {$cond: {if: {$eq: ['$type', 'deal']}, then: 1, else: 0}},")
				  .append("vip: {$cond: {if: {$eq: ['$type', 'vip']}, then: 1, else: 0}},")
				  .append("diamondVip: {$cond: {if: {$eq: ['$type', 'diamondVip']}, then: 1, else: 0}},")
				  .append("crownedVip: {$cond: {if: {$eq: ['$type', 'crownedVip']}, then: 1, else: 0}}")
				  .append("}}");
		
		groupStr.setLength(0);
		groupStr.append("{$group: {")
				.append("_id: {userId: '$call.callUser.userid'},")
				.append("bc20Count: {$sum: '$bc20'},")
				.append("bc40Count: {$sum: '$bc40'},")
				.append("bc60Count: {$sum: '$bc60'},")
				.append("bc80Count: {$sum: '$bc80'},")
				.append("dealCount: {$sum: '$deal'},")
				.append("vipCount: {$sum: '$vip'},")
				.append("diamondVipCount: {$sum: '$diamondVip'},")
				.append("crownedVipCount: {$sum: '$crownedVip'}")
				.append("}}");
		
		opList.clear();
		opList = setAggregationOperation(matchStr,
				projectStr, groupStr);
		
		TypedAggregation<AggregationCustomerAuditRecordModel> auditAggregation = Aggregation.newAggregation(AggregationCustomerAuditRecordModel.class, opList);
		AggregationResults<AggregationCustomerAuditRecordModel> audidtAggregationResult = mongoCrm.aggregate(auditAggregation, "crm_customer_audit_record", AggregationCustomerAuditRecordModel.class);
		List<AggregationCustomerAuditRecordModel> auditModelList = audidtAggregationResult.getMappedResults();
		
		for(AggregationCustomerAuditRecordModel model: auditModelList) {
			KpiIndicators kpiIndicators;
			if(kpiIndicatorsMap.containsKey(model.getUserId())) {
				kpiIndicators = kpiIndicatorsMap.get(model.getUserId());
				setBusinessParam(model, kpiIndicators);
			}else {
				WorkGroup workGroup = userGroupMap.get(model.getUserId());
				User user = userMap.get(model.getUserId());
				kpiIndicators = new KpiIndicators();
				
				kpiIndicators.setReportYear(reportDate.get(Calendar.YEAR));
				kpiIndicators.setReportMonth(reportDate.get(Calendar.MONTH) + 1);
				kpiIndicators.setReportDay(reportDate.get(Calendar.DAY_OF_MONTH));
				kpiIndicators.setGroupId(workGroup.get_id());
				kpiIndicators.setGroupName(workGroup.getName());
				kpiIndicators.setUserId(model.getUserId());
				kpiIndicators.setUsername(user.getUsername());
				kpiIndicators.setUserRealName(user.getRealName());
				setBusinessParam(model, kpiIndicators);
				kpiIndicators.setCtime(cal.getTime());
				kpiIndicatorsList.add(kpiIndicators);
			}
		}
		getReportDatasource().insert(kpiIndicatorsList, KpiIndicators.class);
	}

	private void setBusinessParam(AggregationCustomerAuditRecordModel model,
			KpiIndicators kpiIndicators) {
		kpiIndicators.setBc20Count(model.getBc20Count());
		kpiIndicators.setBc40Count(model.getBc40Count());
		kpiIndicators.setBc60Count(model.getBc60Count());
		kpiIndicators.setBc80Count(model.getBc80Count());
		kpiIndicators.setDealCount(model.getDealCount());
		kpiIndicators.setVipCount(model.getDealCount());
		kpiIndicators.setDiamondVipCount(model.getDiamondVipCount());
		kpiIndicators.setCrownedVipCount(model.getCrownedVipCount());
	}

	private List<AggregationOperation> setAggregationOperation(
			StringBuilder matchStr, StringBuilder projectStr,
			StringBuilder groupStr) {
		final DBObject matcher = (DBObject) JSON.parse(matchStr.toString());
		final DBObject projecter = (DBObject) JSON.parse(projectStr.toString());
		final DBObject grouper = (DBObject) JSON.parse(groupStr.toString());
		
		List<AggregationOperation> opList = new ArrayList<AggregationOperation>();
		opList.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(matcher);
			}
		});
		opList.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(projecter);
			}
		});
		opList.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(grouper);
			}
		});
		return opList;
	}
	
	public static class AggregationCustomerServiceRecordModel {
		private Long userId;
		private int potentialCount;
		private int connectedCount;
		private int proxyConnectedCount;
		private int noResponseCount;
		private int closedCount;
		private int busySignalCount;
		private int refusedCount;
		private int vacantCount;
		private int faxCount;
		private int reAssignCount;
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public int getPotentialCount() {
			return potentialCount;
		}
		public void setPotentialCount(int potentialCount) {
			this.potentialCount = potentialCount;
		}
		public int getConnectedCount() {
			return connectedCount;
		}
		public void setConnectedCount(int connectedCount) {
			this.connectedCount = connectedCount;
		}
		public int getProxyConnectedCount() {
			return proxyConnectedCount;
		}
		public void setProxyConnectedCount(int proxyConnectedCount) {
			this.proxyConnectedCount = proxyConnectedCount;
		}
		public int getNoResponseCount() {
			return noResponseCount;
		}
		public void setNoResponseCount(int noResponseCount) {
			this.noResponseCount = noResponseCount;
		}
		public int getClosedCount() {
			return closedCount;
		}
		public void setClosedCount(int closedCount) {
			this.closedCount = closedCount;
		}
		public int getBusySignalCount() {
			return busySignalCount;
		}
		public void setBusySignalCount(int busySignalCount) {
			this.busySignalCount = busySignalCount;
		}
		public int getRefusedCount() {
			return refusedCount;
		}
		public void setRefusedCount(int refusedCount) {
			this.refusedCount = refusedCount;
		}
		public int getVacantCount() {
			return vacantCount;
		}
		public void setVacantCount(int vacantCount) {
			this.vacantCount = vacantCount;
		}
		public int getFaxCount() {
			return faxCount;
		}
		public void setFaxCount(int faxCount) {
			this.faxCount = faxCount;
		}
		public int getReAssignCount() {
			return reAssignCount;
		}
		public void setReAssignCount(int reAssignCount) {
			this.reAssignCount = reAssignCount;
		}
		@Override
		public String toString() {
			return "AggregationCustomerServiceRecordModel [userId=" + userId
					+ ", potentialCount=" + potentialCount
					+ ", connectedCount=" + connectedCount
					+ ", proxyConnectedCount=" + proxyConnectedCount
					+ ", noResponseCount=" + noResponseCount + ", closedCount="
					+ closedCount + ", busySignalCount=" + busySignalCount
					+ ", refusedCount=" + refusedCount + ", vacantCount="
					+ vacantCount + ", faxCount=" + faxCount
					+ ", reAssignCount=" + reAssignCount + "]";
		}
	}
	
	public static class AggregationCustomerAuditRecordModel {
		private Long userId;
		private int bc20Count;
		private int bc40Count;
		private int bc60Count;
		private int bc80Count;
		private int dealCount;
		private int vipCount;
		private int diamondVipCount;
		private int crownedVipCount;
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public int getBc20Count() {
			return bc20Count;
		}
		public void setBc20Count(int bc20Count) {
			this.bc20Count = bc20Count;
		}
		public int getBc40Count() {
			return bc40Count;
		}
		public void setBc40Count(int bc40Count) {
			this.bc40Count = bc40Count;
		}
		public int getBc60Count() {
			return bc60Count;
		}
		public void setBc60Count(int bc60Count) {
			this.bc60Count = bc60Count;
		}
		public int getBc80Count() {
			return bc80Count;
		}
		public void setBc80Count(int bc80Count) {
			this.bc80Count = bc80Count;
		}
		public int getDealCount() {
			return dealCount;
		}
		public void setDealCount(int dealCount) {
			this.dealCount = dealCount;
		}
		public int getVipCount() {
			return vipCount;
		}
		public void setVipCount(int vipCount) {
			this.vipCount = vipCount;
		}
		public int getDiamondVipCount() {
			return diamondVipCount;
		}
		public void setDiamondVipCount(int diamondVipCount) {
			this.diamondVipCount = diamondVipCount;
		}
		public int getCrownedVipCount() {
			return crownedVipCount;
		}
		public void setCrownedVipCount(int crownedVipCount) {
			this.crownedVipCount = crownedVipCount;
		}
		@Override
		public String toString() {
			return "AggregationCustomerAuditRecordModel [userId=" + userId
					+ ", bc20Count=" + bc20Count + ", bc40Count=" + bc40Count
					+ ", bc60Count=" + bc60Count + ", bc80Count=" + bc80Count
					+ ", dealCount=" + dealCount + ", vipCount=" + vipCount
					+ ", diamondVipCount=" + diamondVipCount
					+ ", crownedVipCount=" + crownedVipCount + "]";
		}
	}
}
