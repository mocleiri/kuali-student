package org.kuali.maventest.modules;

import junit.framework.Assert;

import org.junit.Test;

public class HelloWorldTest {

	@Test
	public void message() {
		HelloWorld hw = new HelloWorld();
		hw.setMessage("hello world");
		Assert.assertEquals("hello world", hw.getMessage());
	}

}
