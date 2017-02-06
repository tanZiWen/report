/**
 * 
 */
package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.prosnav.report.core.BaseParamModel;
import com.prosnav.report.core.IReportDataSourceProvider;


/**
 * @author wangnan
 *
 */
abstract class AbstractProvider<T extends BaseParamModel> implements IReportDataSourceProvider<T> {

	@Override
	public JRDataSource getReportDataSource(T paramModel) {
		return  new JRBeanCollectionDataSource(new ArrayList<Object>());
	}

	@Override
	public com.prosnav.report.core.IReportDataSourceProvider.DateType getInitDateType() {
		return IReportDataSourceProvider.DateType.DAY;
	}

	@Override
	public Date getInitDate() {
		return new Date();
	}

	@Override
	public boolean isDayEnable() {
		return true;
	}

	@Override
	public boolean isMonthEnable() {
		return true;
	}

	@Override
	public boolean isYearEnable() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.prosnav.report.core.IReportDataSourceProvider#isBeginDateEnable()
	 */
	@Override
	public boolean isBeginDateEnable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.core.IReportDataSourceProvider#isEndDateEnable()
	 */
	@Override
	public boolean isEndDateEnable() {
		return true;
	}

	@Override
	public Map<String, Object> getReportParameters(BaseParamModel paramModel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reportDateBegin", paramModel.getSelectionBeginDate());
		params.put("reportDateEnd", paramModel.getSelectionEndDate());
		return params;
	}

}
