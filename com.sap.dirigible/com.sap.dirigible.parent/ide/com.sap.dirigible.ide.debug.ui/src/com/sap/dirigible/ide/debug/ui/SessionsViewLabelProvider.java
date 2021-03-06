/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.ide.debug.ui;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class SessionsViewLabelProvider extends AbstractDebugLabelProvider implements ITableLabelProvider {
	
	private static final long serialVersionUID = 2684004047978059155L;

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return columnIndex == 0 ? getImage() : null;
	}

	public Image getImage() {
		return createImage(DEBUG_SESSION_ICON_URL);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof String) {
			return (String) element;
		}
		return "invalid session";
	}

}