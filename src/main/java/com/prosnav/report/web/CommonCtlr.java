package com.prosnav.report.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosnav.report.core.ReportException;
import com.prosnav.report.domain.db.upm.Function;
import com.prosnav.report.domain.db.upm.Role;
import com.prosnav.report.domain.db.upm.User;
import com.prosnav.report.service.CommonService;

@Controller
public class CommonCtlr {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate mongoUpm;
	
	@Autowired
	private CommonService commonService;
	
	@Value("${session.check:true}")
	private boolean sessionCheck;
	
	@RequestMapping(value="/")
	public String index(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		User sessionUser = (User) session.getAttribute("sessionUser");
		Function root = commonService.buildMenuTree("REPORT", sessionUser.getFunctionList());
		model.addAttribute("menuTree", root);
		return "index";
	}
	
	@RequestMapping(value="/timeout")
	public String timeout() {
		return "timeout";
	}
	
	@RequestMapping(value="/login")
	public String login(@RequestParam(required=false) String token, @RequestParam(required=false) String username, HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = null;
		if(sessionCheck) {
			if(StringUtils.isEmpty(token)) {
				return "redirect:/timeout";
			}
			ValueOperations<String, String> ops = redisTemplate.opsForValue();
			String userStr = ops.get("ps:user:" + token);
			if(StringUtils.isEmpty(userStr)) {
				return "redirect:/timeout";
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			try {
				user = mapper.readValue(userStr, User.class);
			} catch (Exception e) {
				throw new ReportException("Build session user failed.", e);
			} 
		} else {
			if(StringUtils.isEmpty(username)) {
				return "redirect:/timeout";
			}
			user = mongoUpm.findOne(new Query(Criteria.where("username").is(username)), User.class);
			List<Role> roleList = mongoUpm.find(new Query(Criteria.where("code").in(Arrays.asList(user.getRoleCodes()))), Role.class);
			user.setRoleList(roleList);
			Map<String, Role> roleMap = new HashMap<String, Role>();
			List<String> funcCodes = new ArrayList<String>();
			for(Role r : roleList) {
				funcCodes.addAll(Arrays.asList(r.getFnCodes()));
				roleMap.put(r.getCode(), r);
			}
			user.setRoleMap(roleMap);
			List<Function> functionList = mongoUpm.find(new Query(Criteria.where("code").in(funcCodes)), Function.class);
			user.setFunctionList(functionList);
			Map<String, Function> functionMap = new HashMap<String, Function>();
			for(Function f : functionList) {
				functionMap.put(f.getCode(), f);
			}
			user.setFunctionMap(functionMap);
		}
		session.setAttribute("sessionUser", user);
		return "redirect:/";
	}
}
