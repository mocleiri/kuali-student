
/*
 * 
 */

package org.kuali.student.wsdl.course;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.10
 * Wed Sep 08 11:26:43 EDT 2010
 * Generated source version: 2.2.10
 * 
 */


@WebServiceClient(name = "CourseService", 
                  wsdlLocation = "file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/CourseService.wsdl",
                  targetNamespace = "http://student.kuali.org/wsdl/course") 
public class CourseService_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://student.kuali.org/wsdl/course", "CourseService");
    public final static QName CourseServicePort = new QName("http://student.kuali.org/wsdl/course", "CourseServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/CourseService.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/CourseService.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public CourseService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CourseService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CourseService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns CourseService
     */
    @WebEndpoint(name = "CourseServicePort")
    public CourseService getCourseServicePort() {
        return super.getPort(CourseServicePort, CourseService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CourseService
     */
    @WebEndpoint(name = "CourseServicePort")
    public CourseService getCourseServicePort(WebServiceFeature... features) {
        return super.getPort(CourseServicePort, CourseService.class, features);
    }

}
