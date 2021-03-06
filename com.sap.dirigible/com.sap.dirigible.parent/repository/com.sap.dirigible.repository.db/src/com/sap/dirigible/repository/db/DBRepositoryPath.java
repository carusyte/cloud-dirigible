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

package com.sap.dirigible.repository.db;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Utility class representing the path object in the Repository
 * 
 */
public class DBRepositoryPath {

	private String path;

	private final String[] segments;

	public DBRepositoryPath(String path) {
		this.path = path;
		final StringTokenizer tokenizer = new StringTokenizer(path,
				DBRepository.PATH_DELIMITER);
		segments = new String[tokenizer.countTokens()];
		for (int i = 0; i < segments.length; ++i) {
			segments[i] = tokenizer.nextToken();
		}
	}

	public DBRepositoryPath(DBRepositoryPath repositoryPath) {
		this(repositoryPath.segments);
	}

	public DBRepositoryPath(String[] segments) {
		this.segments = Arrays.copyOf(segments, segments.length);
		this.path = toString();
	}

	/**
	 * Getter for the last segment
	 * 
	 * @return
	 */
	public String getLastSegment() {
		if (segments.length == 0) {
			return ""; //$NON-NLS-1$
		}
		return segments[segments.length - 1];
	}

	/**
	 * Getter for the path of the parent
	 * 
	 * @return
	 */
	public DBRepositoryPath getParentPath() {
		if (segments.length == 0) {
			return null;
		}
		final String[] newSegments = Arrays.copyOf(segments,
				segments.length - 1);
		return new DBRepositoryPath(newSegments);
	}

	/**
	 * Add new segment after the last position
	 * 
	 * @param name
	 * @return
	 */
	public DBRepositoryPath append(String name) {
		final String[] newSegments = Arrays.copyOf(segments,
				segments.length + 1);
		newSegments[newSegments.length - 1] = name;
		return new DBRepositoryPath(newSegments);
	}

	@Override
	public String toString() {
		if (segments.length == 0) {
			return DBRepository.PATH_DELIMITER;
		}
		final StringBuilder builder = new StringBuilder();
		for (String segment : segments) {
			builder.append(DBRepository.PATH_DELIMITER);
			builder.append(segment);
		}
		return builder.toString();
	}

	public String getPath() {
		return path;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DBRepositoryPath)) {
			return false;
		}
		final DBRepositoryPath other = (DBRepositoryPath) obj;
		return getPath().equals(other.getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	public String[] getSegments() {
		return Arrays.copyOf(segments, segments.length);
	}
	
	public String constructPath(int number) {
		if (number >= segments.length) {
			return toString();
		}
		if (segments.length == 0) {
			return DBRepository.PATH_DELIMITER;
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < number; i++) {
			builder.append(DBRepository.PATH_DELIMITER);
			builder.append(segments[i]);
		}
		return builder.toString();
	}
}
