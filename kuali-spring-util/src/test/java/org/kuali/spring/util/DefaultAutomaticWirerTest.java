package org.kuali.spring.util;

import java.io.IOException;

import org.junit.Test;

public class DefaultAutomaticWirerTest {

	@Test
	public void wire() throws IOException {
		PropertyHandler handler = new PropertyHandler();
		Wirer wirer = new DefaultAutoWirer(handler);
		wirer.wire();
	}

}
