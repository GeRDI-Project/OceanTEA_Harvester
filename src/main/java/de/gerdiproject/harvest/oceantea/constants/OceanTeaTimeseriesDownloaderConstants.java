/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package de.gerdiproject.harvest.oceantea.constants;

/**
 * Static class with constants for downloading metadata and data from OceanTEA.
 *
 * @author Ingo Thomsen
 */
public class OceanTeaTimeseriesDownloaderConstants {

	/**
	 * static class (therefore private constructor)
	 */
	private OceanTeaTimeseriesDownloaderConstants() {
	}

	//
	// static URLs
	//
	public static final String TIMESERIES_URL = "timeseries/";
	public static final String DATATYPES_URL = "datatypes/";

	// template string
	public static final String DATASET_DOWNLOAD_URL = OceanTeaTimeSeriesDataCiteConstants.BASE_URL + TIMESERIES_URL
			+ "%s/%s/%s/%s";

}
