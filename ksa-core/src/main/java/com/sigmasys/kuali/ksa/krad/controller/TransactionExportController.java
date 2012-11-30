package com.sigmasys.kuali.ksa.krad.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sigmasys.kuali.ksa.krad.form.EmptyForm;
import com.sigmasys.kuali.ksa.service.GeneralLedgerService;
import com.sigmasys.kuali.ksa.service.TransactionExportService;

/**
 * This controller initiates download of the XML file produced as the result
 * of the call to {@link TransactionExportService#exportTransactions()}
 * 
 * @author Sergey
 */
@Controller
@RequestMapping(value = "/exportTransactions")
public class TransactionExportController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(TransactionExportController.class);
    
    @Autowired
    private TransactionExportService transactionExportService;
    
    @Autowired
    private GeneralLedgerService generalLedgerService;
    
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
	@Override
	protected UifFormBase createInitialForm(HttpServletRequest request) {
		return new EmptyForm();
	}

    /**
     * @param form
     * @return
     */
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    @Transactional(readOnly = true)
    public ModelAndView get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	// Prepare the General Ledger transactions:
    	generalLedgerService.prepareGlTransmissions();
    	
    	// Get the XML content for the export:
    	String xml = transactionExportService.exportTransactions();
    	
    	// Do the download process:
    	doDownload(xml, req, resp);
    	
        return null;
    }
    
    /**
     *  Sends content as a String to the ServletResponse output stream.  Typically
     *  you want the browser to receive a different name than the
     *  name the file has been saved in your local database, since
     *  your local names need to be unique.
     *
     *  @param req The request
     *  @param resp The response
     */
    private void doDownload(String content, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        String fileName = "Transaction Export - " + new Date().toString() + ".xml";

        resp.setContentType("application/xml");
        resp.setContentLength(content.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        //
        //  Stream to the requester.
        //
        int bufSize = 1024;
        byte[] bbuf = new byte[bufSize];
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());

        while ((in != null) && ((length = in.read(bbuf)) != -1))
        {
            op.write(bbuf,0,length);
        }

        in.close();
        op.flush();
        op.close();
    }

}
