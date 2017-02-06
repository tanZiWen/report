package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prosnav.report.core.BaseParamModel;
import com.prosnav.report.core.IReportDataSourceProvider;
import com.prosnav.report.domain.db.crm.WechatAccount;
import com.prosnav.report.domain.enums.WechatStatus;
import com.prosnav.report.domain.report.DataWechatContactsStatReportModel;

@Service
public class DataWechatContactsStatReportProvider extends AbstractProvider<BaseParamModel> {

	private final static Logger LOGGER = LogManager.getLogger(DataWechatContactsStatReportProvider.class);
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoCrm;
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	@Override
	public Class<BaseParamModel> getParamModelType() {
		return BaseParamModel.class;
	}

	@Override
	public JRDataSource getReportDataSource(BaseParamModel paramModel) {
		String beginDate = paramModel.getSelectionBeginDateISO();
		//beginDate = "2014-09-01T00:00:01.000Z";
		String endDate = paramModel.getSelectionEndDateISO();
		//endDate = "2014-09-25T10:08:09.000Z";
		StringBuffer match = new StringBuffer()
			.append("{$match : {")
			.append("'wechatAddRecord.ctime' : {$gte : {'$date' : '%1$s'}, $lt : {'$date' : '%2$s'}}")
			.append("}},");
		
		StringBuffer unwind = new StringBuffer()
			.append("{$unwind : '$wechatAddRecord'}");
		
		StringBuffer project = new StringBuffer()
			.append("{$project : {wechatAddRecord : 1}}");
		
		StringBuffer rematch = new StringBuffer()
			.append("{$match : {")
			.append("'wechatAddRecord.ctime' : {$gte : {'$date' : '%1$s'}, $lt : {'$date' : '%2$s'}}")
			.append("}},");
		
		StringBuffer group = new StringBuffer()
			.append("{$group : {")
			.append("_id : {accountId : '$wechatAddRecord.accountId', status : '$wechatAddRecord.wechatStatus'},")
			.append("count : {$sum : 1}")
			.append("}}");
		final DBObject matchObj = (DBObject)JSON.parse (String.format(match.toString(), beginDate, endDate));
		final DBObject unwindObj = (DBObject)JSON.parse(unwind.toString());
		final DBObject projectObj = (DBObject)JSON.parse (project.toString());
		final DBObject rematchObj = (DBObject)JSON.parse (String.format(rematch.toString(), beginDate, endDate));
		final DBObject groupObj = (DBObject)JSON.parse (group.toString());
		List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(matchObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(unwindObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(projectObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(rematchObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(groupObj);
			}
		});
		TypedAggregation<DataWechatContactsStatReportModel> aggregation = Aggregation.newAggregation(
				DataWechatContactsStatReportModel.class, aos
		    );
		AggregationResults<DataWechatContactsStatReportModel> result = mongoCrm.aggregate(aggregation, "crm_customer", DataWechatContactsStatReportModel.class);
		List<DataWechatContactsStatReportModel> list = result.getMappedResults();
		if(list.size() == 0) {
			list = new ArrayList<DataWechatContactsStatReportModel>();
			list.add(new DataWechatContactsStatReportModel());
			return new JRBeanCollectionDataSource(list);
		} else {
			Map<Long, DataWechatContactsStatReportModel> modelMap = new HashMap<Long, DataWechatContactsStatReportModel>();
			for(DataWechatContactsStatReportModel model : list) {
				if(modelMap.containsKey(model.getAccountId())) {
					if(WechatStatus.OPENED.val().equals(model.getStatus())) {
						modelMap.get(model.getAccountId()).setOpenedCount(model.getCount());
					}
					modelMap.get(model.getAccountId()).setTotalCount(modelMap.get(model.getAccountId()).getTotalCount() + model.getCount());
				} else {
					if(WechatStatus.OPENED.val().equals(model.getStatus())) {
						model.setOpenedCount(model.getCount());
					}
					model.setTotalCount(model.getCount());
					modelMap.put(model.getAccountId(), model);
				}
			}
			
			Criteria criteria = Criteria.where("_id").in(modelMap.keySet());
			List<WechatAccount> wechatAccounts = mongoCrm.find(new Query().addCriteria(criteria), WechatAccount.class);
			for(WechatAccount c : wechatAccounts) {
				DataWechatContactsStatReportModel model = modelMap.get(c.get_id());
				if(model != null)
					model.setAccountCode(c.getCode());
			}
			Map<Long, Long> accountCountMap = getSuccessWechatList(beginDate, endDate);
			for(Long accountId : modelMap.keySet()) {
				if(accountCountMap.containsKey(accountId)) {
					modelMap.get(accountId).setSuccessCount(accountCountMap.get(accountId));;
				} else {
					modelMap.get(accountId).setSuccessCount(0l);
				}
			}
			return new JRBeanCollectionDataSource(modelMap.values());
		}
		
	}
	
	private Map<Long, Long> getSuccessWechatList(String beginDate, String endDate) {
		StringBuffer match = new StringBuffer()
			.append("{$match : {")
			.append("wechatManager : {$exists:true}, ")
			.append("'wechatManager.ctime' : {$gte : {'$date' : '%1$s'}, $lt : {'$date' : '%2$s'}}")
			.append("}},");
	
		StringBuffer project = new StringBuffer()
			.append("{$project : {wechatManager : 1}}");
	
		StringBuffer group = new StringBuffer()
			.append("{$group : {")
			.append("_id : {accountId : '$wechatManager.accountId'}, ")
			.append("count : {$sum : 1}")
			.append("}}");
		final DBObject matchObj = (DBObject)JSON.parse (String.format(match.toString(), beginDate, endDate));
		final DBObject projectObj = (DBObject)JSON.parse (project.toString());
		final DBObject groupObj = (DBObject)JSON.parse (group.toString());
		List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(matchObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(projectObj);
			}
		});
		aos.add(new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return context.getMappedObject(groupObj);
			}
		});
		TypedAggregation<DataWechatContactsStatReportModel> aggregation = Aggregation.newAggregation(
				DataWechatContactsStatReportModel.class, aos
		    );
		AggregationResults<DataWechatContactsStatReportModel> result = mongoCrm.aggregate(aggregation, "crm_customer", DataWechatContactsStatReportModel.class);
		List<DataWechatContactsStatReportModel> list = result.getMappedResults();
		Map<Long, Long> accountCountMap = new HashMap<Long, Long>();
		for(DataWechatContactsStatReportModel model : list) {
			accountCountMap.put(model.getAccountId(), model.getCount());
		}
		return accountCountMap;
	}

	@Override
	public com.prosnav.report.core.IReportDataSourceProvider.DateType getInitDateType() {
		return IReportDataSourceProvider.DateType.DAY;
	}

	@Override
	public Date getInitDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	@Override
	public Map<String, Object> getReportParameters(BaseParamModel paramModel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reportDateBegin", paramModel.getSelectionBeginDate());
		params.put("reportDateEnd", paramModel.getSelectionEndDate());
		return params;
	}
}
