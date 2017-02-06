package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prosnav.report.domain.db.upm.Function;

@Service
public class CommonService {
	
	@Autowired
	@Qualifier("mongoTemplateUpm")
	private MongoTemplate upmTemplate;
	
	public Function buildMenuTree(String appCode, List<Function> functions) {
		List<String> fnCodes = new ArrayList<String>();
		for(Function f : functions) {
			fnCodes.add(f.getCode());
		}
		List<Function> fnList = (List<Function>) upmTemplate.find(
				Query.query(Criteria.where("appCode").is(appCode).and("code").in(fnCodes)), 
				Function.class
			);
		return buildMenuRoot(fnList);
	}
	
	private Function buildMenuRoot(List<Function> functions) {
		Function root = new Function();
		root.setCode("root");
		root.setType("menu");
		buildMenu(root, functions);
		return root;
	}
	
	private void buildMenu(Function parent, List<Function> functions) {
		if(!StringUtils.isEmpty(parent.getType()) 
				&& (parent.getType().equals("menu") || parent.getType().equals("fn"))) {
			List<Function> children = getMenuChildren(parent, functions);
			for(Function child : children) {
				buildMenu(child, functions);
			}
		} else {
			return;
		}
		
	}
	
	private List<Function> getMenuChildren(Function parent, List<Function> functions) {
		for(Function f : functions) {
			if(!StringUtils.isEmpty(parent.getCode()) && parent.getCode().equals(f.getParentCode())) {
				parent.getChildren().add(f);
			}
		}
		return parent.getChildren();
	}
}
