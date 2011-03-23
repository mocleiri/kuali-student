package org.kuali.spring.util;

public interface VisitationListener {
	public void beforeBeanVisit(BeanVisitationEvent event);

	public void afterBeanVisit(BeanVisitationEvent event);

	public void valueResolved(ValueResolutionEvent event);
}
