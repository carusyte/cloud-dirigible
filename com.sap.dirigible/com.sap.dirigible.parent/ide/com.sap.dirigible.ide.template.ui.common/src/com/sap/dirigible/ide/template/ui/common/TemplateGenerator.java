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

package com.sap.dirigible.ide.template.ui.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.template.velocity.VelocityGenerator;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.ui.viewer.WorkspaceViewerUtils;

public abstract class TemplateGenerator {

	private static final String THE_FILE_ALREADY_EXISTS_SKIPPED_GENERATION_OF = Messages.TemplateGenerator_THE_FILE_ALREADY_EXISTS_SKIPPED_GENERATION_OF;

	private static final Logger logger = Logger
			.getLogger(TemplateGenerator.class);

	private VelocityGenerator velocityGenerator = new VelocityGenerator();

	private List<IFile> createdFiles = new ArrayList<IFile>();

	protected abstract GenerationModel getModel();

	protected abstract Map<String, Object> prepareParameters();

	protected abstract String getLogTag();

	public void generate() throws Exception {
		generateFile(getModel().getTemplateLocation(), getModel()
				.getTargetLocation(), getModel().getFileName());
	}

	public void generateFile(String templateLocation, String targetLocation,
			String fileName) throws Exception {
		Map<String, Object> parameters = prepareParameters();
		InputStream in = this.getClass().getResourceAsStream(templateLocation);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		velocityGenerator.generate(in, out, parameters, getLogTag());
		byte[] bytes = out.toByteArray();
		bytes = afterGeneration(bytes);
		IPath location = new Path(targetLocation).append(fileName);
		createFile(location, bytes);
	}

	// default implementation - do nothing
	protected byte[] afterGeneration(byte[] bytes) {
		return bytes;
	}

	protected void createFile(IPath location, byte[] bytes) throws Exception {
		IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IFile file = root.getFile(location);
		if (file.exists()) {
			// TODO add as Markers as well
			logger.warn(String.format(THE_FILE_ALREADY_EXISTS_SKIPPED_GENERATION_OF, location));
		} else {
			file.create(new ByteArrayInputStream(bytes), false, null);
			createdFiles.add(file);
		}
		IContainer parent = file.getParent();
		if (parent != null) {
			WorkspaceViewerUtils.expandElement(parent);
		}
		WorkspaceViewerUtils.selectElement(file);
	}

	protected void copyFile(String targetFileName, String templateLocation,
			Class<?> clazz) throws IOException, Exception {
		IPath location = new Path(getModel().getTargetLocation())
				.append(targetFileName);
		InputStream in = clazz.getResourceAsStream(templateLocation);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		createFile(location, out.toByteArray());
	}

	public List<IFile> getGeneratedFiles() {
		return createdFiles;
	}

	public VelocityGenerator getVelocityGenerator() {
		return velocityGenerator;
	}
}
