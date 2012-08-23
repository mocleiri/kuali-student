package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.krad.form.AdminLiaisonForm;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.service.TransactionImportService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kns.exception.FileUploadLimitExceededException;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * User: dmulderink
 * Date: 4/24/12
 * Time: 11:17 AM
 */
@Controller
@RequestMapping(value = "/adminLiaisonVw")
public class AdminLiaisonController extends UifControllerBase {

   @Autowired
   private TransactionService transactionService;

   @Autowired
   private TransactionImportService transactionImportService;

   @Autowired
   private ConfigService configService;

   private static final Log logger = LogFactory.getLog(AdminLiaisonController.class);

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected AdminLiaisonForm createInitialForm(HttpServletRequest request) {
      AdminLiaisonForm form = new AdminLiaisonForm();
      form.setUploadProcessState("");
      return form;
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") AdminLiaisonForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */

   @RequestMapping(params = "methodToCall=start")
   public ModelAndView start(@ModelAttribute("KualiForm") AdminLiaisonForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

      // populate model for testing

      return super.start(form, result, request, response);

   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=submit")
   @Transactional(readOnly = false)
   public ModelAndView submit(@ModelAttribute("KualiForm") AdminLiaisonForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...
      //org.springframework.web.multipart.MaxUploadSizeExceededException: Maximum upload size of 500000 bytes exceeded; nested exception is org.apache.commons.fileupload.FileUploadBase$SizeLimitExceededException: the request was rejected because its size (992410) exceeds the configured maximum (500000)
      String processMsg = "";
      File xmlDestFile = null;
      try {
         MultipartFile uploadXMLFile = form.getUploadXMLFile();
         String fileName = uploadXMLFile.getName();
         String orgFileName = uploadXMLFile.getOriginalFilename();
         String contentType = uploadXMLFile.getContentType();
         if (contentType.endsWith("xml")) {

            try {
               InputStream xmlStream = uploadXMLFile.getInputStream();
               StringBuffer strContent = new StringBuffer("");

                  System.out.println("Total file size to read (in bytes) : "
                        + xmlStream.available());

                  int content;
                  while ((content = xmlStream.read()) != -1) {
                     // convert to char and append to string buffer
                     strContent.append((char) content);
                  }
               xmlStream.close();

               String xmlContent = strContent.toString();

               processMsg = "Processing Transaction(s)";
               form.setUploadProcessState(processMsg);
               String processResponse = transactionImportService.processTransactions(xmlContent);

               logger.info("Response: \n" + processResponse);
               String begValue = "<batch-status>";
               String endValue = "</batch-status>";
               int begIndex = processResponse.indexOf(begValue) + begValue.length();
               int endIndex = processResponse.indexOf(endValue);
               String batchStatus = processResponse.substring(begIndex, endIndex);
               processMsg = "Transaction(s) Processing " + batchStatus;
               form.setUploadProcessState(processMsg);
            } catch (IOException ioExp) {
               String expMsg = ioExp.getMessage();
               logger.error(expMsg);
            }
            catch (Exception exp) {
               String expMsg = exp.getMessage();
               logger.error(expMsg);
               processMsg = "Transaction(s) Processing Failed";
               form.setUploadProcessState(processMsg);
            }
         }
         else {
            processMsg = "Only XML files are allowed. Please try again.";
            form.setUploadProcessState(processMsg);
            logger.error(processMsg);
         }
      }
      catch (FileUploadLimitExceededException exp) {
         String expMsg = exp.getMessage();
         logger.error(expMsg);
         form.setUploadProcessState("File size limit exceeded");
      } finally {
      }

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
   @Transactional(readOnly = false)
   public ModelAndView save(@ModelAttribute("KualiForm") AdminLiaisonForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

      // do save stuff...

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=cancel")
   public ModelAndView cancel(@ModelAttribute ("KualiForm") AdminLiaisonForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do cancel stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute ("KualiForm") AdminLiaisonForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }
}
