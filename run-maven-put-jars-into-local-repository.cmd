echo this will put the j6plugin jar into the local repository
pause
call D:\apache\apache-maven-3.0.4\bin\mvn install:install-file -DgroupId=net.pandoragames -DartifactId=j6plugin -Dversion=0.2 -Dpackaging=jar -DgeneratePom=true -Dfile=j6plugin-0.2.jar
pause
