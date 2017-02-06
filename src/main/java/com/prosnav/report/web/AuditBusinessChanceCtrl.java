package com.prosnav.report.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prosnav.report.domain.db.upm.Organization;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.domain.db.upm.WorkGroup;
import com.prosnav.report.service.AuditBusinessChanceReportService;

@Controller
public class AuditBusinessChanceCtrl {
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	@Autowired
	private AuditBusinessChanceReportService dataMgrBusinessChanceReportService;
	
	@RequestMapping(value="/rpt/dataMgrBusinessChanceReportParam/loadParam", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> loadParam(@RequestParam(required=false) String orgCodeValue, @RequestParam(required=false) String workGroupValue, HttpServletRequest req, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Organization> rootOrgList = dataMgrBusinessChanceReportService.getRootOrgList();
		map.put("rootOrgList", rootOrgList);
		
		List<WorkGroup> workGroupList = dataMgrBusinessChanceReportService.getWorkGroupList(orgCodeValue, workGroupValue);
		map.put("workGourpList", workGroupList);
		
		List<Long> userIdList = dataMgrBusinessChanceReportService.getUserIdList(workGroupList);
		
		List<User> userList = dataMgrBusinessChanceReportService.getUserList(userIdList);
		map.put("userList", userList);
		
		return map;
	}
}
