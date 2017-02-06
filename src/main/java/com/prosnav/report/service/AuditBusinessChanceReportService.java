package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.prosnav.report.domain.db.upm.Organization;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.db.upm.WorkGroup;
import com.prosnav.report.domain.db.upm.WorkGroup.Worker;

@Service
public class AuditBusinessChanceReportService {
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	public List<Organization> getRootOrgList() {
		List<Organization> rootOrgList = mongoUpm.find(new Query(Criteria.where("parentCode").is("root")), Organization.class);
		return rootOrgList;
	}
	
	public List<WorkGroup> getWorkGroupList(String orgCodeValue, String workGroupValue) {
		Query workGourpQuery = new Query();
		if(orgCodeValue != null && orgCodeValue != "") {
			workGourpQuery.addCriteria(Criteria.where("orgCode").is(orgCodeValue));
		}
		
		if(workGroupValue != null && workGroupValue != "") {
			workGourpQuery.addCriteria(Criteria.where("_id").is(Integer.parseInt(workGroupValue)));
		}
		
		List<WorkGroup> workGroupList = mongoUpm.find(workGourpQuery, WorkGroup.class);
		
		return workGroupList;
	}
	
	public List<Long> getUserIdList(List<WorkGroup> workGroupList) {
		List<Long> userIdList = new ArrayList<Long>();
		for(WorkGroup workGroup: workGroupList) {
			Worker[] workers = workGroup.getWorkers();
			for(Worker worker: workers) {
				userIdList.add(worker.getUserid());
			}
		}
		return userIdList;
	}
	
	public List<User> getUserList(List<Long> userIdList) {
		Query userQuery = new Query();
		userQuery.addCriteria(Criteria.where("status").is("ok"));
		userQuery.addCriteria(Criteria.where("_id").in(userIdList));
		List<User> userList = mongoUpm.find(userQuery, User.class);
		return userList;
	}
}
