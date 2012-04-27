package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.AdminLiaisonForm;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
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
import java.io.File;
import java.io.IOException;

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
      try {
         MultipartFile uploadXMLFile = form.getUploadXMLFile();
         String fileName = uploadXMLFile.getName();
         String orgFileName = uploadXMLFile.getOriginalFilename();
         String contentType = uploadXMLFile.getContentType();
         if (contentType.endsWith("xml")) {
            form.setUploadProcessState("File Uploaded Successfully");
            File xmlDestFile = new File("C:\\Vendor\\KSAXmlProcessArea\\" + orgFileName);
            try {
            uploadXMLFile.transferTo(xmlDestFile);
            } catch (IOException exp) {
               String expMsg = exp.getMessage();
               form.setUploadProcessState("File destination exception");
            }
         }
         else {
            form.setUploadProcessState("Only XML files are allowed. Please try again.");
         }
      }
      catch (FileUploadLimitExceededException exp) {
         String expMsg = exp.getMessage();
         form.setUploadProcessState("File size limit exceeded");
      }
      /*catch (FileUploadBase.FileUploadIOException) {

      }*/
/*      catch  (FileUploadException exp) {
         String expMsg = exp.getMessage();
      }*/
      finally {

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
