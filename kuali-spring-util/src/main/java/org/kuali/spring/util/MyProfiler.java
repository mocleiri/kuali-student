package org.kuali.spring.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MyProfiler {

	@Pointcut("execution(* org.kuali.spring.util.*.*(..))")
	public void kualiSpringMethods() {
	}

	@Around("kualiSpringMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		System.out.println("Going to call the method.");
		Object output = pjp.proceed();
		System.out.println("Method execution completed.");
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Method execution time: " + elapsedTime + " milliseconds.");
		return output;
	}

}
