package org.kuali.student.poc.common.test.spring;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {
	public String value();
}
