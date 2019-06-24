package com.vrkb.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.vrkb.bean.User;

public class SessionFilter implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
			throws Exception {
		
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv)
			throws Exception {
		
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		HttpSession session = request.getSession(true);
		//seesion中获取用户名信息
		User user = (User)session.getAttribute("user");
		if(user == null){
			request.setAttribute("message", "timeout");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
//			response.sendRedirect(request.getSession().getServletContext().getContextPath()+"/login.jsp?message=aaaa");
			return false;
		}
		return true;
	}

	
}
