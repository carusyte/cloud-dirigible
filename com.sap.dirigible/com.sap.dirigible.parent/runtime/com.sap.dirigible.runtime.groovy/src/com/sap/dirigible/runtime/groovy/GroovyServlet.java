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

package com.sap.dirigible.runtime.groovy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public class GroovyServlet extends AbstractScriptingServlet {

	private static final long serialVersionUID = 2580668894416114646L;
	
	private static final Logger logger = Logger
			.getLogger(GroovyServlet.class.getCanonicalName());

	protected void doExecution(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String module = request.getPathInfo();

		GroovyExecutor executor = createExecutor(request);
		Map<Object, Object> executionContext = new HashMap<Object, Object>();
		try {
			executor.executeServiceModule(request, response, module, executionContext);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public GroovyExecutor createExecutor(HttpServletRequest request)
			throws IOException {
		
		GroovyExecutor executor = new GroovyExecutor(getRepository(request),
				getScriptingRegistryPath(request), REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor;
	}
}
