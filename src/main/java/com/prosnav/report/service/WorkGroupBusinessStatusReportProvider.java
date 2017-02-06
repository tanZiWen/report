/**
 * 
 */
package com.prosnav.report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.prosnav.report.core.BaseParamModel;
import com.prosnav.report.core.IReportDataSourceProvider;
import com.prosnav.report.domain.db.report.WorkGroupBusinessStatus;

/**
 * @author wangnan
 *
 */
@Service
public class WorkGroupBusinessStatusReportProvider extends
		AbstractProvider<BaseParamModel> {
	
	@Autowired
	@Qualifier("mongoTemplateReport")
	private MongoTemplate mongoReport;

	@Override
	public Class<BaseParamModel> getParamModelType() {
		return BaseParamModel.class;
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#getReportDataSource(com.prosnav.report.core.BaseParamModel)
	 */
	@Override
	public JRDataSource getReportDataSource(BaseParamModel paramModel) {
		String[] endDate = paramModel.getMaxSelectionEndDate();
		if(endDate == null || endDate.length == 0) {
			WorkGroupBusinessStatus model = new WorkGroupBusinessStatus();
			model.setGroupName("");
			model.setUserRealName("");
			return new JRBeanCollectionDataSource(new ArrayList<WorkGroupBusinessStatus>(Arrays.asList(model)));
		}
		Criteria criteria = Criteria
				.where("reportYear").is(Integer.valueOf(endDate[0]))
				.and("reportMonth").is(Integer.valueOf(endDate[1]))
				.and("reportDay").is(Integer.valueOf(endDate[2]));
		List<WorkGroupBusinessStatus> result = mongoReport.find(
				new Query(criteria).with(new Sort("groupId")), 
				WorkGroupBusinessStatus.class);
		List<WorkGroupBusinessStatus> reportList = new ArrayList<WorkGroupBusinessStatus>();
		WorkGroupBusinessStatus total = null;
		WorkGroupBusinessStatus avg = null;
		int count = 0;
		for(WorkGroupBusinessStatus model : result) {
			if(avg != null && avg.getGroupId() != model.getGroupId()) {
				if(count != 0) {
					avg.setPotentialCount(total.getPotentialCount() / count);
					avg.setBc20Count(total.getBc20Count() / count);
					avg.setBc40Count(total.getBc40Count() / count);
					avg.setBc60Count(total.getBc60Count() / count);
					avg.setBc80Count(total.getBc80Count() / count);
					avg.setDealCount(total.getDealCount() / count);
					avg.setVipCount(total.getVipCount() / count);
					avg.setDiamondVipCount(total.getDiamondVipCount() / count);
					avg.setCrownedVipCount(total.getCrownedVipCount() / count);
				}
				reportList.add(avg);	
			}
			if(avg == null || avg.getGroupId() != model.getGroupId()) {
				avg = new WorkGroupBusinessStatus();
				avg.setGroupId(model.getGroupId());
				avg.setGroupName("");
				avg.setUserRealName("平均值");
				count = 0;
			}
			
			if(total != null && total.getGroupId() != model.getGroupId()) {
				reportList.add(total);
			}
			if(total == null || total.getGroupId() != model.getGroupId()) {
				total = new WorkGroupBusinessStatus();
				total.setGroupId(model.getGroupId());
				total.setGroupName("");
				total.setUserRealName("总计");
			}
			
			reportList.add(model);
			++count;
			total.setPotentialCount(total.getPotentialCount() + model.getPotentialCount());
			total.setBc20Count(total.getBc20Count() + model.getBc20Count());
			total.setBc40Count(total.getBc40Count() + model.getBc40Count());
			total.setBc60Count(total.getBc60Count() + model.getBc60Count());
			total.setBc80Count(total.getBc80Count() + model.getBc80Count());
			total.setDealCount(total.getDealCount() + model.getDealCount());
			total.setVipCount(total.getVipCount() + model.getVipCount());
			total.setDiamondVipCount(total.getVipCount() + model.getVipCount());
			total.setCrownedVipCount(total.getCrownedVipCount() + model.getCrownedVipCount());
		}
		return new JRBeanCollectionDataSource(reportList);
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#getInitDateType()
	 */
	@Override
	public com.prosnav.report.core.IReportDataSourceProvider.DateType getInitDateType() {
		return IReportDataSourceProvider.DateType.DAY;
	}
	
	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#isBeginDateEnable()
	 */
	@Override
	public boolean isBeginDateEnable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#isMonthEnable()
	 */
	@Override
	public boolean isMonthEnable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#isYearEnable()
	 */
	@Override
	public boolean isYearEnable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.prosnav.report.service.AbstractProvider#getInitDate()
	 */
	@Override
	public Date getInitDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
}
