/**
 * 
 */
package com.prosnav.report.web;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prosnav.report.core.ReportBatchExecutor;

/**
 * @author wangnan
 *
 */
@Controller
public class DevCtlr {
	
	@Autowired
	private ReportBatchExecutor reportBatchExecutor;
	
	@Value("${gloable.enviroment}")
	private String enviroment;
	
	@RequestMapping("/dev")
	public String show(Model model) {
		model.addAttribute("reportBatchNames", reportBatchExecutor.getReportBatchNames());
		model.addAttribute("enviroment", enviroment);
		return "dev";
	}
	
	@RequestMapping("/dev/executeReportBatch/{name}")
	public @ResponseBody RestResult executeAllReportBatch(@PathVariable String name) {
		RestResult rr = new RestResult();
		reportBatchExecutor.execute(new ArrayList<String>(Arrays.asList(name)));
		rr.setStatus("Finished");
		rr.setMessage("report batch executed : " + name);
		return rr;
	}
	
	
	static class RestResult {
		
		private String status;
		private String message;
		
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
