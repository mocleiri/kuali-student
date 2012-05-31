package com.sigmasys.kuali.ksa.service;

import org.aopalliance.aop.Advice;

import java.util.List;

/**
 * Any bean implementing this interface will be wrapped by BeanPostProcessor with a Spring AOP proxy instance
 *
 * @author Michael Ivanov
 */
public interface AopProxy {

    List<Advice> getAdvices();

}
