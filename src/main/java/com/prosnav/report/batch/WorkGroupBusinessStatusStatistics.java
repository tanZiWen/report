/**
 * 
 */
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
import com.prosnav.report.domain.db.report.WorkGroupBusinessStatus;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.db.upm.WorkGroup;
import com.prosnav.report.domain.db.upm.WorkGroup.Worker;

/**
 * @author wangnan
 *
 */
@Component
public class WorkGroupBusinessStatusStatistics extends AbstractReportBatch<WorkGroupBusinessStatus> {
	
	private final static Logger LOGGER = LogManager.getLogger(WorkGroupBusinessStatusStatistics.class);
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoCrm;
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	public void exec() {
		
		List<WorkGroupBusinessStatus> result = new ArrayList<WorkGroupBusinessStatus>();
		
		Map<Long, WorkGroup> userGroupMap = new HashMap<Long, WorkGroup>();
		Map<Long, User> userMap = new HashMap<Long, User>();
		
		StringBuilder matcherText = new StringBuilder();
		StringBuilder projecterText = new StringBuilder();
		StringBuilder grouperText = new StringBuilder();
		StringBuilder matchUserIdsStr = new StringBuilder();
		
		//find group
		List<WorkGroup> workGroups = mongoUpm.find(new Query(Criteria.where("isDel").ne(true)), WorkGroup.class);
		for(WorkGroup wg : workGroups) {
			for(Worker worker : wg.getWorkers()) {
				if(matchUserIdsStr.length() == 0) {
					matchUserIdsStr.append(worker.getUserid());
				} else {
					matchUserIdsStr.append(",").append(worker.getUserid());
				}
				userGroupMap.put(worker.getUserid(), wg);
			}
		}
		workGroups = null;
		
		//find users
		List<User> users = mongoUpm.find(new Query(Criteria.where("_id").in(userGroupMap.keySet())), User.class);
		for(User user : users) {
			userMap.put(user.get_id(), user);
		}
		users = null;
		
		//statistics
		matcherText.append("{$match : ")
			.append("{belongUser : {$in : [").append(matchUserIdsStr).append("]}, free : false}")
			.append("}");
		projecterText.append("{")
			.append("$project : {")
			.append("belongUser : 1,")
			.append("potential : {$cond : {if : {$eq : ['$status' , 'potential']}, then : 1, else : 0}},")
			.append("bc20 : {$cond : {if : {$eq : ['$status' , 'bc20']}, then : 1, else : 0}},")
			.append("bc40 : {$cond : {if : {$eq : ['$status' , 'bc40']}, then : 1, else : 0}},")
			.append("bc60 : {$cond : {if : {$eq : ['$status' , 'bc60']}, then : 1, else : 0}},")
			.append("bc80 : {$cond : {if : {$eq : ['$status' , 'bc80']}, then : 1, else : 0}},")
			.append("deal : {$cond : {if : {$eq : ['$status' , 'deal']}, then : 1, else : 0}},")
			.append("vip : {$cond : {if : {$eq : ['$status' , 'vip']}, then : 1, else : 0}},")
			.append("diamondVip : {$cond : {if : {$eq : ['$status' , 'diamondVip']}, then : 1, else : 0}},")
			.append("crownedVip : {$cond : {if : {$eq : ['$status' , 'crownedVip']}, then : 1, else : 0}}")
			.append("}")
			.append("}");
		grouperText.append("{")
			.append("$group : {")
			.append("_id : {userId : '$belongUser'},")
			.append("potentialCount : {$sum : '$potential'},")
			.append("bc20Count : {$sum : '$bc20'},")
			.append("bc40Count : {$sum : '$bc40'},")
			.append("bc60Count : {$sum : '$bc60'},")
			.append("bc80Count : {$sum : '$bc80'},")
			.append("dealCount : {$sum : '$deal'},")
			.append("vipCount : {$sum : '$vip'},")
			.append("diamondVipCount : {$sum : '$diamondVip'},")
			.append("crownedVipCount : {$sum : '$crownedVip'}")
			.append("}")
			.append("}");
		
		final DBObject matcher = (DBObject) JSON.parse(matcherText.toString());
		final DBObject projecter = (DBObject) JSON.parse(projecterText.toString());
		final DBObject grouper = (DBObject) JSON.parse(grouperText.toString());
		
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
		
		TypedAggregation<AggregationResultModel> aggregation = Aggregation.newAggregation(AggregationResultModel.class, opList);
		AggregationResults<AggregationResultModel> aggregationResult = mongoCrm.aggregate(aggregation, "crm_customer", AggregationResultModel.class);
		List<AggregationResultModel> modelList = aggregationResult.getMappedResults();
		
		//save result
		Calendar date = Calendar.getInstance();
		Calendar reportDate = Calendar.getInstance();
		reportDate.add(Calendar.DAY_OF_MONTH, -1);
		for(AggregationResultModel model : modelList) {
			WorkGroup group = userGroupMap.get(model.getUserId());
			User user = userMap.get(model.getUserId());
			WorkGroupBusinessStatus reportModel = new WorkGroupBusinessStatus();
			reportModel.setReportYear(reportDate.get(Calendar.YEAR));
			reportModel.setReportMonth(reportDate.get(Calendar.MONTH) + 1);
			reportModel.setReportDay(reportDate.get(Calendar.DAY_OF_MONTH));
			reportModel.setGroupId(group.get_id());
			reportModel.setGroupName(group.getName());
			reportModel.setUserId(user.get_id());
			reportModel.setUsername(user.getUsername());
			reportModel.setUserRealName(user.getRealName());
			reportModel.setPotentialCount(model.getPotentialCount());
			reportModel.setBc20Count(model.getBc20Count());
			reportModel.setBc40Count(model.getBc40Count());
			reportModel.setBc60Count(model.getBc60Count());
			reportModel.setBc80Count(model.getBc80Count());;
			reportModel.setDealCount(model.getDealCount());
			reportModel.setVipCount(model.getVipCount());
			reportModel.setDiamondVipCount(model.getDiamondVipCount());
			reportModel.setCrownedVipCount(model.getCrownedVipCount());
			reportModel.setCtime(date.getTime());
			result.add(reportModel);
		}
		getReportDatasource().insert(result, WorkGroupBusinessStatus.class);
		
	}
	
	public static class AggregationResultModel {
		private Long userId;
		private int potentialCount;
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
		/**
		 * @return the potentialCount
		 */
		public int getPotentialCount() {
			return potentialCount;
		}
		/**
		 * @param potentialCount the potentialCount to set
		 */
		public void setPotentialCount(int potentialCount) {
			this.potentialCount = potentialCount;
		}
		/**
		 * @return the bc20Count
		 */
		public int getBc20Count() {
			return bc20Count;
		}
		/**
		 * @param bc20Count the bc20Count to set
		 */
		public void setBc20Count(int bc20Count) {
			this.bc20Count = bc20Count;
		}
		/**
		 * @return the bc40Count
		 */
		public int getBc40Count() {
			return bc40Count;
		}
		/**
		 * @param bc40Count the bc40Count to set
		 */
		public void setBc40Count(int bc40Count) {
			this.bc40Count = bc40Count;
		}
		/**
		 * @return the bc60Count
		 */
		public int getBc60Count() {
			return bc60Count;
		}
		/**
		 * @param bc60Count the bc60Count to set
		 */
		public void setBc60Count(int bc60Count) {
			this.bc60Count = bc60Count;
		}
		/**
		 * @return the bc80Count
		 */
		public int getBc80Count() {
			return bc80Count;
		}
		/**
		 * @param bc80Count the bc80Count to set
		 */
		public void setBc80Count(int bc80Count) {
			this.bc80Count = bc80Count;
		}
		/**
		 * @return the dealCount
		 */
		public int getDealCount() {
			return dealCount;
		}
		/**
		 * @param dealCount the dealCount to set
		 */
		public void setDealCount(int dealCount) {
			this.dealCount = dealCount;
		}
		/**
		 * @return the vipCount
		 */
		public int getVipCount() {
			return vipCount;
		}
		/**
		 * @param vipCount the vipCount to set
		 */
		public void setVipCount(int vipCount) {
			this.vipCount = vipCount;
		}
		/**
		 * @return the diamondVipCount
		 */
		public int getDiamondVipCount() {
			return diamondVipCount;
		}
		/**
		 * @param diamondVipCount the diamondVipCount to set
		 */
		public void setDiamondVipCount(int diamondVipCount) {
			this.diamondVipCount = diamondVipCount;
		}
		/**
		 * @return the crownedVipCount
		 */
		public int getCrownedVipCount() {
			return crownedVipCount;
		}
		/**
		 * @param crownedVipCount the crownedVipCount to set
		 */
		public void setCrownedVipCount(int crownedVipCount) {
			this.crownedVipCount = crownedVipCount;
		}
		
	}
}
