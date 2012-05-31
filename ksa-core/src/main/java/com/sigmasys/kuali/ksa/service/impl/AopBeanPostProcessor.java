package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.service.AopProxy;
import org.aopalliance.aop.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AopBeanPostProcessor
 *
 * @author Michael Ivanov
 */
@Service("aopBeanPostProcessor")
public class AopBeanPostProcessor implements BeanPostProcessor {

    private static final Log logger = LogFactory.getLog(AopBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AopProxy) {
            List<Advice> advices = ((AopProxy) bean).getAdvices();
            if (advices != null) {
                if (bean instanceof Advised) {
                    Advised advised = (Advised) bean;
                    for (Advice advice : advices) {
                        advised.addAdvice(advice);
                    }
                }
            }
        }
        return bean;
    }

}
