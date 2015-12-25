package xyz.daisong.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import xyz.daisong.constants.Constants;
import xyz.daisong.controller.SecurityController;
import xyz.daisong.service.ZKConnectService;
import xyz.daisong.utils.ServletContext;

public class SecurityInterceptor extends HandlerInterceptorAdapter{
	@Autowired
	private ZKConnectService zkconnectService;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(request.getSession().getAttribute(Constants.CURRENT_USER_SESSION) == null){
			//mock
			ServletContext context = new ServletContext();
			context.setZkClient(zkconnectService.connect());
			ServletContext.setContext(context);
			request.getSession().setAttribute(Constants.CURRENT_USER_SESSION,context);
			//end mock
			
			/*if(handler instanceof HandlerMethod){
				HandlerMethod handlerMethod = (HandlerMethod)handler;
				if(!(handlerMethod.getMethod().getDeclaringClass() == SecurityController.class)){
					response.sendRedirect("loginPage.htm");
					return false;
				}
			}else{//默认请求 404
				response.sendRedirect("loginPage.htm");
				return false;
			}*/
		}
		return super.preHandle(request, response, handler);
	}
}
