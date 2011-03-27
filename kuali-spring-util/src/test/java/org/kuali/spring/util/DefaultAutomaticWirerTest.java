package org.kuali.spring.util;

import java.io.IOException;

import org.junit.Test;

public class DefaultAutomaticWirerTest {

	@Test
	public void wire() throws IOException {
		PropertyHandlerOld handler = new PropertyHandlerOld();
		Wirer wirer = new DefaultAutoWirer(handler);
		wirer.wire();
	}

}
