package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaMemoForm;
import com.sigmasys.kuali.ksa.krad.form.KsaStudentAccountsForm;
import com.sigmasys.kuali.ksa.krad.util.AlertsFlagsMemos;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Information;
import com.sigmasys.kuali.ksa.model.InformationTypeValue;
import com.sigmasys.kuali.ksa.model.Memo;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 10/4/12 at 7:53 AM
 */
@Controller
@RequestMapping(value = "/ksaMemoVw")
public class KsaMemoController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(KsaStudentAccountsController.class);

   @Autowired
   private InformationService informationService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected KsaMemoForm createInitialForm(HttpServletRequest request) {
      KsaMemoForm form = new KsaMemoForm();
      String userId = request.getParameter("userId");

      if (userId != null) {

         Account account = accountService.getFullAccount(userId);

         if (account == null) {
            String errMsg = "Cannot find Account by ID = " + userId;
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
         }

         form.setAccount(account);
      }/* else {
          String errMsg = "'userId' request parameter cannot be null";
          logger.error(errMsg);
          throw new IllegalStateException(errMsg);
       }*/

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
   public ModelAndView get(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {
      // do get stuff...

      String pageId = request.getParameter("pageId");
      // example user 1
      String userId = request.getParameter("userId");
      // a table selection record index
      String transUserId = request.getParameter("id");

      if (pageId != null && pageId.compareTo("MemosPage") == 0) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         List<Memo> memos = informationService.getMemos(userId);

         AlertsFlagsMemos afm = new AlertsFlagsMemos();
         // Alerts
         for (Memo memo : memos) {

            memo.setCompositeInfo(afm.CreateCompositeMemo(memo));
         }

         form.setMemos(memos);
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

   @RequestMapping(params = "methodToCall=start")
   public ModelAndView start(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
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
   public ModelAndView submit(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...

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
   public ModelAndView save(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") KsaMemoForm form, BindingResult result,
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=insertMemo")
   public ModelAndView insertMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do insert stuff...

      // TODO validate the field entries before inserting

      // the account should be satisfied by the link that got us into this page
      Account account = form.getAccount();

      String infoType = InformationTypeValue.MEMO.name();

      InformationTypeValue informationType = Enum.valueOf(InformationTypeValue.class, infoType);

      Memo memo = form.getMemo();
      Information info =  memo;
      memo.setAccount(account);
      memo.setAccountId(account.getId());
      memo.setCreationDate(new Date());
      memo.setLastUpdate(new Date());
      //info.setCreatorId();
      //info.setCreatorId();
      Long infoId = informationService.persistInformation(memo);

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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=updateMemo")
   public ModelAndView updateMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=followUpMemo")
   public ModelAndView followUpMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }
}
