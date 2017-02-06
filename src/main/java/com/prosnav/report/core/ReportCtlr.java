package com.prosnav.report.core;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.prosnav.report.domain.db.upm.Function;
import com.prosnav.report.domain.db.upm.User;

@Controller
public class ReportCtlr<T extends IParamModel> {
	
	private static final Logger LOGGER = LogManager.getLogger(ReportCtlr.class);
	
	@RequestMapping("/rpt/generate/{reportName}")
	public String generate(@PathVariable String reportName, 
			@RequestParam String format, 
			@RequestParam("provider") String providerName, 
			HttpServletRequest req, Model model) {
		IReportDataSourceProvider<IParamModel> provider = getProvider(
				reportName, providerName, req);
		IParamModel paramModel = null;
		try {
			Class<?> clazz = Class.forName(provider.getParamModelType().getName());
			paramModel = (IParamModel) clazz.newInstance();
		} catch (Exception e) {
			ReportException ex = new ReportException("Can't new instance of type : " + provider.getParamModelType().getName(), e);
			LOGGER.error(ex);
			throw ex;
		}
		ServletRequestDataBinder requestBinder = new ServletRequestDataBinder(paramModel);
		requestBinder.bind(req);
		model.addAttribute("datasource", provider.getReportDataSource(paramModel));
		model.addAttribute("format", format);
		Map<String, Object> parameters = provider.getReportParameters(paramModel);
		if(parameters != null) {
			for(String key : parameters.keySet()) {
				model.addAttribute(key, parameters.get(key));
			}
		}
		return reportName;
	}

	private IReportDataSourceProvider<IParamModel> getProvider(
			String reportName, String providerName, HttpServletRequest req) {
		if(StringUtils.isEmpty(providerName)) {
			providerName = reportName + "Provider";
		}
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());
		@SuppressWarnings("unchecked")
		IReportDataSourceProvider<IParamModel> provider = (IReportDataSourceProvider<IParamModel>) context.getBean(providerName);
		return provider;
	}
	
	@RequestMapping("/rpt/show")
	public String show(@RequestParam String reportName, 
			@RequestParam String code, 
			@RequestParam(required=false) String paramFormPage, 
			@RequestParam(required=false) String providerName,
			HttpServletRequest req,
			Model model) {
		User sessionUser = (User) req.getSession().getAttribute("sessionUser");
		Function fn = sessionUser.getFunctionMap().get(code);
		model.addAttribute("reportName", reportName);
		model.addAttribute("displayName", fn.getName());
		model.addAttribute("code", fn.getCode());
		if(StringUtils.isEmpty(providerName)) {
			model.addAttribute("provider", "");
		} else {
			model.addAttribute("provider", providerName);
		}
		IReportDataSourceProvider<IParamModel> provider = getProvider(reportName, providerName, req);
		model.addAttribute("initDateType", provider.getInitDateType() == null ? "" : provider.getInitDateType().name());
		model.addAttribute("initDate", provider.getInitDate() == null ? "" : String.valueOf(provider.getInitDate().getTime()));
		model.addAttribute("dayEnable", String.valueOf(provider.isDayEnable()));
		model.addAttribute("monthEnable", String.valueOf(provider.isMonthEnable()));
		model.addAttribute("yearEnable", String.valueOf(provider.isYearEnable()));
		model.addAttribute("beginDateEnable", String.valueOf(provider.isBeginDateEnable()));
		model.addAttribute("endDateEnable", String.valueOf(provider.isEndDateEnable()));
		
		if(StringUtils.isEmpty(paramFormPage)) {
			model.addAttribute("paramFormPage", "");
		} else {
			model.addAttribute("paramFormPage", paramFormPage);
		}
		return "report/report";
	}
	
	@RequestMapping("/rpt/param/show")
	public String showParamPage(@RequestParam String paramFormPage) {
		return "report/param/" + paramFormPage;
	}
}
