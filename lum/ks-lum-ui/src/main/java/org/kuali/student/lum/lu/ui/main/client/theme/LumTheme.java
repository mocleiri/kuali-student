/*
 * Copyright 2009 Johnson Consulting Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.kuali.student.lum.lu.ui.main.client.theme;

import com.google.gwt.core.client.GWT;

/**
 * This interface defines the base class required for implementing a theme
 * @author wilj
 *
 */
public interface LumTheme {
	public static final LumTheme INSTANCE = GWT.create(LumTheme.class);

	public LumImages getLumImages();
	public LumCss getLumCss();
	public LumWidgets getLumWidgets();
}
