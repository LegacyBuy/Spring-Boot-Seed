package com.legacybuy.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Log4j
@Aspect
@Component
public class LoggerAspect {

	@Before("execution(* com.legacybuy..*.*(..))")
	public void logMethodAccessBefore(JoinPoint joinPoint) {
		log.debug(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " | Args=> " + Arrays.asList(joinPoint.getArgs()));
	}
	
	@AfterReturning(value = "execution(* com.legacybuy..*.*(..))", returning = "returnValue")
    public void logMethodAccessAfter(JoinPoint joinPoint, Object returnValue) {
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		if(signature.getReturnType() == void.class){
			returnValue = "Void";
		}
		
		log.debug(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " | Response=> " + returnValue);
    }
	
	@AfterThrowing(value = "execution(* com.legacybuy..*.*(..))", throwing="exception")
    public void logMethodAccessAfterThrowing(JoinPoint joinPoint, Exception exception) {
		log.error(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " | Exception=> ", exception);
    }
	
	
}
