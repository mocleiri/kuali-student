package com.sigmasys.kuali.ksa.krad.util;

import com.sigmasys.kuali.ksa.model.Alert;
import com.sigmasys.kuali.ksa.model.Flag;
import com.sigmasys.kuali.ksa.model.Memo;

import java.text.SimpleDateFormat;

/**
 * User: dmulderink
 * Date: 4/25/12
 * Time: 11:41 AM
 */
public class AlertsFlagsMemos {

   public String CreateCompositeAlert(Alert alert) {

      String compositeAlert = "";

      if (alert != null) {

         StringBuilder sba = new StringBuilder();

         //sba.append(alert.getCreationDate().toString());
         SimpleDateFormat dtFrmt = new SimpleDateFormat("MM/dd/yyyy");
         StringBuilder sbDt = new StringBuilder(dtFrmt.format(alert.getCreationDate()));
         String infoDate = sbDt.toString();
         sba.append(infoDate);

         sba.append(" ");
         sba.append(alert.getText());
         compositeAlert = sba.toString();
      }

      return compositeAlert;
   }

   public String CreateCompositeMemo(Memo memo) {

      String compositeMemo = "";

      if (memo != null) {

         StringBuilder sba = new StringBuilder();

         //sba.append(alert.getCreationDate().toString());
         SimpleDateFormat dtFrmt = new SimpleDateFormat("MM/dd/yyyy");
         StringBuilder sbDt = new StringBuilder(dtFrmt.format(memo.getCreationDate()));

         String infoDate = sbDt.toString();
         sba.append(infoDate);

         sba.append("  ");
/*         sba.append(dtFrmt.format(memo.getEffectiveDate()));
         sba.append("  ");
         sba.append(dtFrmt.format(memo.getLastUpdate()));
         sba.append("  ");*/
         sba.append(memo.getText());
         compositeMemo = sba.toString();
      }

      return compositeMemo;
   }

   public String CreateCompositeFlag(Flag flag) {

      String compositeFlag = "";

      if (flag != null) {

         StringBuilder sba = new StringBuilder();

         //sba.append(alert.getCreationDate().toString());
         SimpleDateFormat dtFrmt = new SimpleDateFormat("MM/dd/yyyy");
         StringBuilder sbDt = new StringBuilder(dtFrmt.format(flag.getCreationDate()));
         String infoDate = sbDt.toString();
         sba.append(infoDate);

         sba.append(" ");
         //sba.append(flag.getText()); TODO
         compositeFlag = sba.toString();
      }

      return compositeFlag;
   }
}
