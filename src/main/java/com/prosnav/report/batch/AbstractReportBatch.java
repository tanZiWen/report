/**
 * 
 */
package com.prosnav.report.batch;

import java.lang.reflect.ParameterizedType;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import com.prosnav.report.core.IReportBatch;
import com.prosnav.report.domain.db.report.BaseReportModel;

/**
 * @author wangnan
 *
 */
abstract public class AbstractReportBatch<T extends BaseReportModel> implements IReportBatch<BaseReportModel> {

	@Autowired
	@Qualifier("mongoTemplateReport")
	private MongoTemplate mongoReport;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean beforeExec() {
		Calendar cal = Calendar.getInstance();
		Class<T> entityClass= (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		T entity = getReportDatasource().findOne(new Query(Criteria
				.where("reportYear").is(cal.get(Calendar.YEAR))
				.and("reportMonth").is(cal.get(Calendar.MONTH) + 1)
				.and("reportDay").is(cal.get(Calendar.DAY_OF_MONTH))), entityClass);
		if(StringUtils.isEmpty(entity)) {
			return true;
		}
		return false;
	}

	public MongoTemplate getReportDatasource() {
		return this.mongoReport;
	}

}
