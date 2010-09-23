package org.kuali.student.schemagen;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Enumerated;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SchemaGen {

	private String outputPath;
	private String[] persistenceFileNames;
	
	public SchemaGen(String outputPath, String[] persistenceFileNames) {
		this.outputPath = outputPath==null?"":outputPath+"/";
		new File(this.outputPath+"oracle").mkdirs();
		new File(this.outputPath+"mysql").mkdirs();
		
		this.persistenceFileNames = persistenceFileNames;
	}
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SchemaGen schemaGen = new SchemaGen("target/gen",new String[]{"META-INF/lu-persistence.xml","META-INF/lo-persistence.xml"});
		schemaGen.generateAllDbTypes();
	}
	public void generateAllDbTypes() throws Exception{
		Map<String,Set<Class<?>>> entries = getClasses(persistenceFileNames);
		for(Entry<String,Set<Class<?>>> entry:entries.entrySet()){
			generate("oracle","org.hibernate.dialect.Oracle10gDialect",entry.getKey(),entry.getValue());
			generate("mysql","org.hibernate.dialect.MySQLDialect",entry.getKey(),entry.getValue());
		}
	}
	
	public void generate(String dbType, String dbDialect, String puName, Set<Class<?>> classes) throws MappingException, HibernateException, Exception{
		//For each persistence unit found, create schema
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		for(Class<?> clazz:classes){
			cfg.addAnnotatedClass(clazz);
		}
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", dbDialect);
		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		
		export.setOutputFile(outputPath+dbType+"/"+puName+"_drop_" + dbType + ".sql");
		export.execute(true, false, true, false);
		export.setOutputFile(outputPath+dbType+"/"+puName+"_create_" + dbType + ".sql");
		export.execute(true, false, false, true);
	}

	private Map<String,Set<Class<?>>> getClasses(String[] persistenceFileNames)
			throws Exception {
		Map<String,Set<Class<?>>> results = new HashMap<String,Set<Class<?>>>();
		for(String persistenceFileName:persistenceFileNames){
			URL url = Thread.currentThread().getContextClassLoader().getResource(persistenceFileName);
			if(url==null){
				throw new RuntimeException("Persistence context file not found:"+persistenceFileName);
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
			doc.getDocumentElement().normalize();
			NodeList unitNodes = doc.getElementsByTagName("persistence-unit");
			for (int i = 0; i < unitNodes.getLength(); i++) {
				Node unitNode = unitNodes.item(i);
				String unitName = unitNode.getAttributes().getNamedItem("name").getNodeValue();
				NodeList classNodes =((Element)unitNode).getElementsByTagName("class");
				Set<Class<?>> classes = new HashSet<Class<?>>();
				for (int j = 0; j < classNodes.getLength(); j++) {
					Node node = classNodes.item(j);
					Class<?> clazz = null;
					clazz = Class.forName(node.getTextContent());
					classes.add(clazz);
					for(Field f:clazz.getDeclaredFields()){
						if(f.isAnnotationPresent(Enumerated.class)){
							classes.add(f.getDeclaringClass());
						}
					}
				}
				results.put(unitName, classes);
			}

		}
		return results;
	}
}
