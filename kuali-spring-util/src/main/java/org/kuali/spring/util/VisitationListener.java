package org.kuali.spring.util;

public interface VisitationListener {
	public void beforeVisit(PropertyValueVisitationEvent event);

	public void afterVisit(PropertyValueVisitationEvent event);

	public void beforeVisit(BeanVisitationEvent event);

	public void afterVisit(BeanVisitationEvent event);

	public void valueResolved(ValueResolutionEvent event);
}
