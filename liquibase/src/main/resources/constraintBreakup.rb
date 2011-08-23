require 'xmlsimple'


# module config is expected in this format:  <relative path to module file name (without xml extension)>:<table name>
# example: atp/ks-enroll-atp-create-schema:KSEN_ATP

moduleConfigFileName = 'riceTables.txt'
schemaFileName = 'constraints.xml'
dataHeader = '<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">'
remainingChangeSetsFile = 'processedConstraints.xml'

@fileNameSuffix = '-constraints.xml'
@constraintTags = { 'addPrimaryKey' => 'tableName', 'addUniqueConstraint' => 'tableName', 'addForeignKeyConstraint' => 'baseTableName'}

configFile = File.open(moduleConfigFileName, 'r')

tableModules = Hash.new
moduleFileHandles = Hash.new

configFile.each_line do |line|
	parts = line.split(':')
	
	tableName = parts[1].strip
	fileName = parts[0].strip
	
	moduleFile = moduleFileHandles[fileName]
	
	if moduleFile == nil
	
		dirName = fileName.split('/')[0]
	
		if !Dir.exist? dirName
			Dir.mkdir dirName
		end
		
		moduleFile = File.new(fileName + @fileNameSuffix, 'w')
		
		moduleFile.puts dataHeader
		
		moduleFileHandles[fileName] = moduleFile
		
	end
	
	tableModules[tableName] = moduleFile
end

schema = XmlSimple.xml_in(schemaFileName)

elementsToRemove = Array.new

@constraintTags.each do |tagName, tableAttribute|
	
	schema['changeSet'].each do |changeSet|
		
		
		if changeSet[tagName] == nil
			next
		end
		
		tableName = changeSet[tagName][0][tableAttribute]
		
		outFile = tableModules[tableName]
		
		if outFile == nil
			#puts 'NO MODULE FOUND FOR TABLE: ' + tableName
		else
			XmlSimple.xml_out(changeSet, {'RootName' => 'changeSet', 'OutputFile' => outFile})
			
			elementsToRemove << changeSet
		end
		
	end

end

moduleFileHandles.each_key do |key|
	moduleFileHandles[key].puts '</databaseChangeLog>'
	moduleFileHandles[key].close
end

prunedSets = schema['changeSet'] - elementsToRemove

newXml = {'changeSet' => prunedSets}

remainingFile = File.new(remainingChangeSetsFile, 'w')
remainingFile.puts dataHeader
XmlSimple.xml_out(newXml, {'RootName' => 'databaseChangeLog', 'OutputFile' => remainingFile})