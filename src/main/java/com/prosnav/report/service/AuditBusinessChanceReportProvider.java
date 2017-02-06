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
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.report.AuditBusinessChanceReportModel;

@Service
public class AuditBusinessChanceReportProvider extends AbstractProvider<BaseParamModel> {

	private final static Logger LOGGER = LogManager.getLogger(AuditBusinessChanceReportProvider.class);
	
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
			.append("'type' : {$in : ['bc20', 'bc40']},")
			.append("'call.callTime' : {$gte : {'$date' : '%1$s'}, $lte : {'$date' : '%2$s'}}")
			.append("}},");
		StringBuffer project = new StringBuffer()
			.append("{$project : {")
			.append("user : '$call.callUser.userid',")
			.append("bc20 : {")
			.append("$cond : {")
			.append("if : {$eq : ['$type', 'bc20']}, then : 1, else : 0")
			.append("}")
			.append("},")
			.append("audit20 : {")
			.append("$cond : {")
			.append("if : {$eq : ['$done', true], $eq : ['$audit.result', 'ok'], $eq : ['$audit.customerStatus', 'bc20']}, then : 1, else : 0")
			.append("}")
			.append("},")
			.append("bc40 : {")
			.append("$cond : {")
			.append("if : {$eq : ['$type', 'bc40']}, then : 1, else : 0")
			.append("}")
			.append("},")
			.append("audit40 : {")
			.append("$cond : {")
			.append("if : {$eq : ['$done', true], $eq : ['$audit.result', 'ok'], $eq : ['$audit.customerStatus', 'bc40']}, then : 1, else : 0")
			.append("}")
			.append("}")
			.append("}},");
		StringBuffer group = new StringBuffer()
			.append("{$group : {")
			.append("_id : {userId : '$user'},")
			.append("bc20Count : {$sum : '$bc20'},")
			.append("audit20Count : {$sum : '$audit20'},")
			.append("bc40Count : {$sum : '$bc40'},")
			.append("audit40Count : {$sum : '$audit40'},")
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
		TypedAggregation<AuditBusinessChanceReportModel> aggregation = Aggregation.newAggregation(
				AuditBusinessChanceReportModel.class, aos
		    );
		AggregationResults<AuditBusinessChanceReportModel> result = mongoCrm.aggregate(aggregation, "crm_customer_audit_record", AuditBusinessChanceReportModel.class);
		List<AuditBusinessChanceReportModel> list = result.getMappedResults();
		if(list.size() == 0) {
			list = new ArrayList<AuditBusinessChanceReportModel>();
			list.add(new AuditBusinessChanceReportModel());
		} else {
			List<Long> ids = new ArrayList<Long>();
			Map<Long, AuditBusinessChanceReportModel> modelMap = new HashMap<Long, AuditBusinessChanceReportModel>();
			for(AuditBusinessChanceReportModel model : list) {
				ids.add(model.getUserId());
				modelMap.put(model.getUserId(), model);
			}
			Criteria criteria = Criteria.where("_id").in(ids);
			List<User> users = mongoUpm.find(new Query().addCriteria(criteria), User.class);
			for(User u : users) {
				AuditBusinessChanceReportModel model = modelMap.get(u.get_id());
				model.setUsername(u.getUsername());
				model.setRealName(u.getRealName());
			}
		}
		return new JRBeanCollectionDataSource(list);
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
