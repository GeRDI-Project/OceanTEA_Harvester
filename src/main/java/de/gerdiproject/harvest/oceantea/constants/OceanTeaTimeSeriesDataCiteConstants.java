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

import java.util.Arrays;
import java.util.List;

import de.gerdiproject.json.datacite.Contributor;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.enums.ContributorType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.NameType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.nested.PersonName;


/**
 * This static class contains constants used for creating DataCite documents for OceanTEA.
 *
 * @author Ingo Thomsen
 */
public class OceanTeaTimeSeriesDataCiteConstants {

	// static class (therefore private constructor)
	private OceanTeaTimeSeriesDataCiteConstants() {
	}

	// language used
	public static final String LANG = "en-US";

	// GEOMAR as standard CREATOR and CONTRIBUTOR
	public static String GEOMAR = "GEOMAR, Kiel, Germany";
	public static final PersonName GEOMAR_PERSON = new PersonName(GEOMAR, NameType.Organisational);
	public static final String MOLAB_PUBLICATION_LINK = "https://oceanrep.geomar.de/22245/";
	public static final List<Creator> CREATORS = Arrays.asList(new Creator(GEOMAR_PERSON));
	public static final List<Contributor> CONTRIBUTORS = Arrays
			.asList(new Contributor(GEOMAR_PERSON, ContributorType.Producer));

	// OceanTEA demo as repository (+ GEOMAR for MoLab reference)
	public static final String OCEANTEA_DEMO_BASE_URL = "http://maui.se.informatik.uni-kiel.de:9090/";
	public static final String PROVIDER = "OceanTEA demo, Software Engineering Informatik, Kiel University";
	public static final String REPOSITORY_ID = "OCEANTEA";
	public static final List<String> RELATED_WEB_LINKS = Arrays.asList(OCEANTEA_DEMO_BASE_URL, MOLAB_PUBLICATION_LINK);

	// Format of ResourceType - there is only one for OceanTEA
	public static final ResourceType RESOURCE_TYPE = new ResourceType("JSON", ResourceTypeGeneral.Dataset);
	public static final List<String> FORMATS = Arrays.asList("text/json");

	// description, disciples and subjects
	public static final List<String> SUBJECT_STRINGS = Arrays.asList("MoLab", "modular ocean laboratory");
	public static final List<String> DISCIPLINES = Arrays.asList("marine measurements");
	public static final Description DESCRIPTION_COMMON = new Description(
			"Underwater measurements captured by a MoLab device (modular ocean laboratory) by " + GEOMAR,
			DescriptionType.Abstract, LANG);

	//
	// template strings
	//
	public static final String MAIN_DOCUMENT_TITLE = "%s measurements, underwater (depth %s m) in the region '%s'";
	public static final String REASEARCH_DATA_LABEL = "%s measurements, collected underwater (depth %s m) "
			+ "in the open water region '%s' in %s by MoLab device";
	public static final String DESCRIPTION = String.join(" ", 
			"%s timeseries data, relative (in seconds) to timestamp '%s'.",
			"Data was collected in the open water region '%s': " + 		"geo location %s at a depth of %s m"
			);
}



