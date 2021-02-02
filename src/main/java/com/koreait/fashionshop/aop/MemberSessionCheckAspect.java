package com.koreait.fashionshop.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreait.fashionshop.exception.LoginRequiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;


//앞으로 로그인이 필요한 서비스 여부를 처리하기 위한 코드는, 컨트롤러에 두지 않고,
//지금 이 객체로 공통화시켜 AOP를 적용하겠다!

public class MemberSessionCheckAspect  {
	private static final Logger logger=LoggerFactory.getLogger(MemberSessionCheckAspect.class);
	
	public Object sessionCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		
		Object target = joinPoint.getTarget(); //원래 호출하려고 했던 객체
		logger.debug("원래 호출하려했던 객체는"+target);//maincontroller
		String methodName = joinPoint.getSignature().getName();
		Object[]args = joinPoint.getArgs(); //원래 호출하려 했던 메서드의 매개변수 (하나는 request, 하나는 name)
		for(Object arg : args) {
			logger.debug("매개변수명은"+arg);
		}
		
		
		//세션을 얻기 위해서는 HttpServletRequest가 필요하다.
		HttpServletRequest request = null;
		for(Object arg:args) {
			logger.debug("매개변수명은"+args);
			if(arg instanceof HttpServletRequest) {//request 객체를 만나면!
				request=(HttpServletRequest)arg;
			}
		}
		
		//현재의 요청이 세션을 가지고 있는지를 판단하여, 적절한 제어를 해야한다.
		
		//1.세션이 없다면? 예외를 발생! -->ExceptionHandle를 거쳐서 클라이언트에게 적절한 응답처리
		//2.세션이 있으면? 그대로 원래 호출하려했던 메서드 진행
		HttpSession session = null;
		session = request.getSession();
		Object result=null;
		
		if(session.getAttribute("member")==null) {
			throw new LoginRequiredException("로그인이 필요한 서비스입니다.");
		}else {
			
			//원래 요청의흐름을 그대로 진행.. 
			result=joinPoint.proceed(); //여기서 예외 발생하므로 예외처리하지말고 throw!
			//메인메서드가 리턴한 mav를 대신 result에 담아준다. 이친구를 작성하지 않으면,mav를 던져주지 않기때문에 빈화면이 나온다.
		}
		
		
		
		return result;
	}
}
