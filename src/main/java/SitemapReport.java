import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: SW Genis
 * Date: 2013/12/04
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SitemapReport {

    /**
     *
     */
    public static void main(String[] args) {
        SitemapReport report = new SitemapReport();
        try {
            report.compile();
            report.fill();
            report.pdf();
            report.html();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     */
    public void compile() throws JRException {

        JasperCompileManager.compileReportToFile("src/main/resources/reports/SitemapReport.jrxml");
        JasperCompileManager.compileReportToFile("src/main/resources/reports/SitemapSubReport.jrxml");
    }

    /**
     *
     */
    public void fill() throws JRException {

        long start = System.currentTimeMillis();
        Map params = new HashMap();
        Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream("src/main/resources/data/sitemap.xml"));
        params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
        params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
        params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
        params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
        params.put(JRParameter.REPORT_LOCALE, Locale.US);
        params.put("APP_URL", "env3.ks.kuali.org");

        JasperFillManager.fillReportToFile("src/main/resources/reports/SitemapReport.jasper", params);
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }

    /**
     *
     */
    public void pdf() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile("src/main/resources/reports/SitemapReport.jrprint");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }

    /**
     *
     */
    public void html() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToHtmlFile("src/main/resources/reports/SitemapReport.jrprint");
        System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
    }

}
