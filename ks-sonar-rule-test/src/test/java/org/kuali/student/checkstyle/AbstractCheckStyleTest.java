/**
 * 
 */
package org.kuali.student.checkstyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

/**
 * Provides some helpers for evaluating CheckStyle Rules 
 */
public abstract class AbstractCheckStyleTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractCheckStyleTest.class);
	
	/**
	 * 
	 */
	public AbstractCheckStyleTest() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param configurationName
	 * @param fileToProcess
	 * @param checkName 
	 * @param checkAttributes 
	 * @param outputFile
	 * @return the number of errors found.
	 * @throws FileNotFoundException
	 * @throws CheckstyleException 
	 */
	protected int processCheckStyle (File fileToProcess, String checkName, Map<String, String> checkAttributes, final String assertCheckKey) throws FileNotFoundException, CheckstyleException {
		 final Configuration config = createConfiguration(checkName, checkAttributes);
		 
	        // setup the output stream

	        
//	        final List<File> files = getFilesToProcess(line);
	        final Checker c = createChecker(config);
	        
	        c.addListener(new AuditListener() {
				
				@Override
				public void fileStarted(AuditEvent aEvt) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void fileFinished(AuditEvent aEvt) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void auditStarted(AuditEvent aEvt) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void auditFinished(AuditEvent aEvt) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void addException(AuditEvent aEvt, Throwable aThrowable) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void addError(AuditEvent aEvt) {
					String errorKey = aEvt.getLocalizedMessage().getKey();
					
					Assert.assertEquals(assertCheckKey, errorKey);
				}
			});
	        
	        final int numErrs = c.process(Arrays.asList(fileToProcess));
	        c.destroy();
	        
	        return numErrs;
	}

	private Configuration createConfiguration(String checkName, Map<String, String>checkAttributes) {
		
		DefaultConfiguration top = new DefaultConfiguration("Checker");
		
		DefaultConfiguration walker;
		top.addChild(walker = new DefaultConfiguration("TreeWalker"));
		
		DefaultConfiguration module = new DefaultConfiguration(checkName);
		
		walker.addChild(module);
		
		for (String key : checkAttributes.keySet()) {
			
			String value = checkAttributes.get(key);
		
			module.addAttribute(key, value);
			
		}
		
		return top;
	}

	/**
     * Creates the Checker object.
     *
     * @param aConfig the configuration to use
     * @param aNosy the sticky beak to track what happens
     * @return a nice new fresh Checker
	 * @throws CheckstyleException 
     */
    private Checker createChecker(Configuration aConfig) throws CheckstyleException
    {
		Checker c = new Checker();

		final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
		c.setModuleClassLoader(moduleClassLoader);
		c.configure(aConfig);
		return c;
    }
}
