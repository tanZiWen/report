package com.prosnav.report.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;

import com.prosnav.report.core.IReportDataSourceProvider;
import com.prosnav.report.domain.report.TestReportModel;
import com.prosnav.report.domain.view.TestReportParam;

@Service
public class TestReportProvider extends AbstractProvider<TestReportParam> {

	@Override
	public JRDataSource getReportDataSource(TestReportParam paramModel) {
		List<TestReportModel> modelList = new ArrayList<TestReportModel>();
		modelList.add(new TestReportModel(1, 20, "u1", "name1"));
		modelList.add(new TestReportModel(1, 20, "u2", "name2"));
		modelList.add(new TestReportModel(1, 20, "u3", "name3"));
		modelList.add(new TestReportModel(1, 20, "u4", "name4"));
		modelList.add(new TestReportModel(1, 20, "u5", "name5"));
		modelList.add(new TestReportModel(1, 20, "u6", "name6"));
		modelList.add(new TestReportModel(1, 20, "u7", "name7"));
		modelList.add(new TestReportModel(1, 20, "u8", "name8"));
		modelList.add(new TestReportModel(1, 20, "u9", "name9"));
		modelList.add(new TestReportModel(1, 20, "u10", "name10"));
		modelList.add(new TestReportModel(1, 20, "u11", "name11"));
		modelList.add(new TestReportModel(1, 20, "u12", "name12"));
		modelList.add(new TestReportModel(1, 20, "u13", "name13"));
		modelList.add(new TestReportModel(1, 20, "u14", "name14"));
		modelList.add(new TestReportModel(1, 20, "u15", "name15"));
		modelList.add(new TestReportModel(1, 20, "u16", "name16"));
		modelList.add(new TestReportModel(1, 20, "u17", "name17"));
		modelList.add(new TestReportModel(1, 20, "u18", "name18"));
		modelList.add(new TestReportModel(1, 20, "u19", "name19"));
		modelList.add(new TestReportModel(1, 20, "u20", "name20"));
		return new JRBeanCollectionDataSource(modelList);
	}

	@Override
	public Class<TestReportParam> getParamModelType() {
		return TestReportParam.class;
	}

	@Override
	public com.prosnav.report.core.IReportDataSourceProvider.DateType getInitDateType() {
		return IReportDataSourceProvider.DateType.YEAR;
	}

	@Override
	public Date getInitDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	@Override
	public Map<String, Object> getReportParameters(TestReportParam paramModel) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		params.put("reportDate", df.format(getInitDate()));
		return params;
	}

	@Override
	public boolean isDayEnable() {
		return false;
	}

	@Override
	public boolean isMonthEnable() {
		return false;
	}

	@Override
	public boolean isYearEnable() {
		return true;
	}
	
	
}
