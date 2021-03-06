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

package com.sap.dirigible.ide.db.publish;

import static com.sap.dirigible.ide.db.publish.DatabaseConstants.REGISTYRY_PUBLISH_LOCATION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.db.DatabaseUpdater;
import com.sap.dirigible.repository.ext.db.DsvUpdater;

public class DatabasePublisher extends AbstractPublisher implements IPublisher {

	private static final String DOT = ".";
	private static final Logger logger = Logger
			.getLogger(DatabasePublisher.class);

	public DatabasePublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, REGISTYRY_PUBLISH_LOCATION);
			final IFolder sourceFolder = getSourceFolder(project,
					ICommonConstants.ARTIFACT_TYPE.DATA_STRUCTURES);
			copyAllFromTo(sourceFolder, targetContainer);

			IRepository repository = RepositoryFacade.getInstance()
					.getRepository();
			DataSource dataSource = DataSourceFacade.getInstance()
					.getDataSource();

			processTablesAndViews(targetContainer, repository, dataSource);
			processDSV(targetContainer, repository, dataSource);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new PublishException(ex.getMessage(), ex);
		}
	}
	
	// no sandboxing for database artifacts
	@Override
	public void activate(IProject project) throws PublishException {
		publish(project);
	}
	
	@Override
	public void activateFile(IFile file) throws PublishException {
		publish(file.getProject());		
	}

	private void processTablesAndViews(final ICollection targetContainer,
			IRepository repository, DataSource dataSource) throws IOException,
			Exception {
		List<String> knownFiles = new ArrayList<String>();
		DatabaseUpdater databaseUpdater = new DatabaseUpdater(repository,
				dataSource, REGISTYRY_PUBLISH_LOCATION);
		databaseUpdater.enumerateKnownFiles(targetContainer, knownFiles);
		databaseUpdater.executeUpdate(knownFiles);
	}

	private void processDSV(ICollection targetContainer,
			IRepository repository, DataSource dataSource) throws IOException,
			Exception {
		List<String> knownFiles = new ArrayList<String>();
		DsvUpdater dsvUpdater = new DsvUpdater(repository, dataSource,
				REGISTYRY_PUBLISH_LOCATION);
		dsvUpdater.enumerateKnownFiles(targetContainer, knownFiles);
		dsvUpdater.executeUpdate(knownFiles);
	}

	@Override
	public String getFolderType() {
		return ICommonConstants.ARTIFACT_TYPE.DATA_STRUCTURES;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		if (checkFolderType(file)) {
			if (DatabaseUpdater.EXTENSION_TABLE.equals(DOT
					+ file.getFileExtension())
					|| DatabaseUpdater.EXTENSION_VIEW.equals(DOT
							+ file.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getPublishedContainerMapping(IFile file) {
		return null;
	}
	
	@Override
	public String getActivatedContainerMapping(IFile file) {
		return null;
	}

	@Override
	public boolean isAutoActivationAllowed() {
		return false;
	}
	
	@Override
	protected String getSandboxLocation() {
		return null;
	}
	
}
