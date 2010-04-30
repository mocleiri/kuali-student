set LUMUI=C:\svn\ks-lum\ks-lum-ui\target\ks-lum-ui-1.0.0-SNAPSHOT.jar
set LIB=lib
set SAXON=%LIB%\saxon-8.7.jar
set CLI=%LIB%\commons-cli-1.2.jar
set KS-COMMON-IMPL=%LIB%\ks-common-impl-1.0.0-SNAPSHOT.jar
set LOG4J=%LIB%\log4j-1.2.14.jar
set SPRING-CORE=%LIB%\spring-core-2.5.4.jar
set SPRING-CONTEXT=%LIB%\spring-context-2.5.4.jar
set SPRING-BEANS=%LIB%\spring-beans-2.5.4.jar
set COMMONS-LOGGING=%LIB%\commons-logging-1.1.1.jar

set CP=.;target/classes;%LUMUI%;%SAXON%;%CLI%;%KS-COMMON-IMPL%;%LOG4J%;%SPRING-CORE%;%SPRING-CONTEXT%;%SPRING-BEANS%;%COMMONS-LOGGING%
set PKG=org.kuali.student.lum.lu.assembly.data.client.refactorme.orch
set CLASS=BrowseCourseCatalogMetadata
set OUT_FILE=orch-dictionary.xml
java  -cp %CP% org.kuali.student.common.util.MetaTransformer -p %PKG% -c %CLASS% -o %OUT_FILE%
pause
