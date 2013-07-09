/*
 * Copyright 2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.pmd.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.AbstractJavaRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.ast.*;
import net.sourceforge.pmd.symboltable.Scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team (ks.collab@kuali.org)
 * 
 * A custom PMD Java Rule to find usages of a StringBuilder outside of a loop.
 *
 * 
 */
public class FindStringBuilderUsageOutSideOfALoop extends AbstractJavaRule {

	private static final Logger log = LoggerFactory.getLogger(FindStringBuilderUsageOutSideOfALoop.class);
	
	/**
	 * 
	 */
	public FindStringBuilderUsageOutSideOfALoop() {
		// TODO Auto-generated constructor stub
	}
	
	private Map<String, Map<String, Class<?>>>scopeToVariableToTypeMap = new HashMap<String, Map<String,Class<?>>>();
	
	@Override
	public Object visit(ASTName node, Object data) {
		// TODO Auto-generated method stub
		
		String name = node.getImage();
		
		if (name != null) {
			String[] parts = name.split("\\.");
			
			if (parts.length == 2) {
				String variableName = parts[0];
				String methodName = parts[1];
				
				
				if (methodName.equals("append")) {
					
					ASTStatement statement = node.getFirstParentOfType(ASTStatement.class);
					
					Scope scope = statement.getScope();
					
					Map<String, Class<?>> variableMap = scopeToVariableToTypeMap.get(scope.toString());
				
					Class<?> clazz = variableMap.get(variableName);
				
					log.info("");
				}

			} else {
				log.warn("parts.length = " + parts.length + " for name = "
						+ name);
			}
			
		}
		
		List<ASTBlock> blocks = node.getParentsOfType(ASTBlock.class);
		
		if (blocks.size() > 0) {
			
			ASTBlock block = blocks.get(0);
			log.info("found a parent block");
			
		}
		return super.visit(node, data);
	}



	@Override
	public Object visit(ASTClassOrInterfaceType node, Object data) {
		
		Class<?>type = node.getType();
		
		log.info("node type = " + type);
		
		ASTFieldDeclaration fieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
		
		if (fieldDeclaration != null) {
			// this is a field declaration
			ASTVariableDeclaratorId delarator = fieldDeclaration.getFirstChildOfType(ASTVariableDeclaratorId.class);
			
			if (delarator != null) {
				Scope scope = delarator.getScope();
				
				String variableName = delarator.getImage();
				
				if (scope != null) {
					String scopeString = scope.toString();
					log.warn("handle scope");
					Map<String, Class<?>> variableToTypeMap = scopeToVariableToTypeMap.get(scopeString);
					
					if (variableToTypeMap == null) {
						variableToTypeMap = new HashMap<String, Class<?>>();
						scopeToVariableToTypeMap.put(scopeString, variableToTypeMap);
					}
					
					variableToTypeMap.put(variableName, type);
				}
				else {
					log.info("scope is null");
				}
					
			}
		}
		else {
			// try next kind
			ASTLocalVariableDeclaration localVaraibleDeclaration = node.getFirstParentOfType(ASTLocalVariableDeclaration.class);
			
			if (localVaraibleDeclaration != null) {
				
				ASTVariableDeclaratorId delarator = localVaraibleDeclaration.getFirstChildOfType(ASTVariableDeclaratorId.class);
				
				Scope scope = delarator.getScope();
				
				String variableName = delarator.getImage();
				
				if (scope != null) {
					String scopeString = scope.toString();
					log.warn("handle scope");
					Map<String, Class<?>> variableToTypeMap = scopeToVariableToTypeMap.get(scopeString);
					
					if (variableToTypeMap == null) {
						variableToTypeMap = new HashMap<String, Class<?>>();
						scopeToVariableToTypeMap.put(scopeString, variableToTypeMap);
					}
					
					variableToTypeMap.put(variableName, type);
				}
				else {
					log.info("scope is null");
				}
			}
			else 
				log.warn("localvaraibledelcaration is null");
		}
		return null;
	}



//	@Override
//	public Object visit(ASTVariableDeclarator node, Object data) {
//		
//		// should have two children
//		// ASTVariableDeclaratorId
//		// ASTVariableInitializer
//		log.info("ASTVariableDeclarator node.type = " + node.getType());
//		
//		if (node.getType() == StringBuilder.class) {
//			
//			ASTVariableDeclaratorId declarator = node.getFirstChildOfType(ASTVariableDeclaratorId.class);
//			
//			ASTVariableInitializer initializer = node.getFirstChildOfType(ASTVariableInitializer.class);
//			
//			log.info("found a string builder");
//		}
//		
//		
//		return super.visit(node, data);
//	}
	
	


}
