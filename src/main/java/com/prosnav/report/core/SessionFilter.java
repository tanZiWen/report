package com.prosnav.report.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.ServletUtil;

import com.prosnav.report.domain.db.upm.User;

public class SessionFilter implements Filter {
	
	private List<String> excludeList;
	private boolean sessionCheck;

	@Override
	public void destroy() {
		excludeList = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		boolean check = true;
		boolean isChain = true;
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		String url = request.getRequestURI();
		
		for(String s : excludeList) {
			if(url.indexOf(s) != -1) {
				check = false;
				break;
			}
		}
		
		if(!this.sessionCheck) {
			check = false;
		}
		
		if(check) {
			HttpSession session = request.getSession();
			User sessionUser = (User) session.getAttribute("sessionUser");
			if (sessionUser == null) {
				isChain = false;
				if(ServletUtil.isAjaxRequest((HttpServletRequest) req)) {
					PrintWriter out = res.getWriter();
					out.print("{error : {code : 'sessionTimeout', msg : '会话超时'}}");
					out.flush();
					out.close();
				} else {
					httpResponse.sendRedirect(req.getServletContext().getContextPath() + "/timeout");
				}
			}
		}
		if(isChain) {
			chain.doFilter(req, res);			
		}
	}

	@SuppressWarnings("null")
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.excludeList = new ArrayList<String>();
		String exclude = config.getInitParameter("exclude");
		if(exclude != null || exclude.trim() != "") {
			String[] excludeArry = exclude.split(",");
			for(String s : excludeArry) {
				excludeList.add(s);
			}
		}
		String check = config.getInitParameter("check");
		if(check != null && check.equals("true")) {
			this.sessionCheck = true;
		} else {
			this.sessionCheck = false;
		}
	}

}
