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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.gerdiproject.json.datacite.Contributor;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.enums.ContributorType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.NameType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.abstr.AbstractResearch;
import de.gerdiproject.json.datacite.extension.constants.ResearchDisciplineConstants;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;
import de.gerdiproject.json.datacite.nested.PersonName;

/**
 * This static class contains constants and some template strings used for
 * creating DataCite documents for OceanTEA.
 *
 * @author Ingo Thomsen
 */
public final class OceanTeaTimeSeriesDataCiteConstants
{

    // language used for data and description
    public static final String LANG = "en-US";

    //
    // GEOMAR as standard CREATOR and CONTRIBUTOR
    //
    public static final String GEOMAR = "GEOMAR, Kiel, Germany";
    public static final PersonName GEOMAR_PERSON = new PersonName(GEOMAR, NameType.Organisational);
    public static final String MOLAB_PUBLICATION_LINK = "https://oceanrep.geomar.de/22245/";
    public static final List<Creator> CREATORS = asUnmodifiableList(new Creator(GEOMAR_PERSON));
    public static final List<Contributor> CONTRIBUTORS = asUnmodifiableList(
                                                             new Contributor(GEOMAR_PERSON, ContributorType.Producer));

    //
    // OceanTEA demo as repository (+ GEOMAR for MoLab reference)
    //
    public static final String BASE_URL = "http://maui.se.informatik.uni-kiel.de:9090/";
    public static final String PROVIDER = "OceanTEA demo, Software Engineering Informatik, Kiel University";
    public static final String REPOSITORY_ID = "OCEANTEA";
    public static final List<WebLink> WEB_LINKS = createRelatedWebLinks(MOLAB_PUBLICATION_LINK);
    public static final String VIEW_URL = BASE_URL;

    //
    // Format of ResourceType - there is only JSON for OceanTEA
    //
    public static final ResourceType RESOURCE_TYPE = new ResourceType("JSON", ResourceTypeGeneral.Dataset);
    public static final String JSON_MIME_MEDIA_TYPE = "application/json";
    public static final List<String> FORMATS = asUnmodifiableList(JSON_MIME_MEDIA_TYPE);

    //
    // description, disciples and subjects
    //
    public static final List<Subject> SUBJECTS = createSubjects("MoLab", "modular ocean laboratory",
                                                                "underwater measurement", "oceanography");

    public static final List<AbstractResearch> DISCIPLINES = asUnmodifiableList(
                                                                 ResearchDisciplineConstants.OCEANOGRAPHY);
    public static final List<Description> DESCRIPTIONS = asUnmodifiableList(new Description(
                                                                                "Underwater measurements captured by a MoLab device (modular ocean laboratory) by " + GEOMAR,
                                                                                DescriptionType.Abstract, LANG));

    //
    // template strings
    //
    public static final String MAIN_DOCUMENT_TITLE = "%s measurements, underwater (depth %.1f m) in the region '%s'";
    public static final String REASEARCH_DATA_LABEL = "%s measurements, collected underwater (depth %.1f m) "
                                                      + "in the open water region '%s' by MoLab device %s";
    public static final String GEO_LOCATION_AS_STRING = "(%.4f;%.4f)";
    public static final String DESCRIPTION = "%s time series data (from %s to %s) with a mean of %.3f %s."
                                             + " %d measurements are provided in the JSON format - relative (in seconds) to timestamp %s."
                                             + " Data was collected in the open water region '%s' at geo location %s at a depth of %.1f m.";
    public static final String DESCRIPTION_MISSING_VALUES_SUFFIX = " %d measurements points were missing ('NA').";
    public static final String PUBLICATION_YEAR_SIMPLE_DATE_FORMAT_STRING = "yyyy";
    public static final String GEOLOCATION_PLACE_DESCRIPTION = "measurement region of %s";

    /**
     * static class (therefore private constructor)
     */
    private OceanTeaTimeSeriesDataCiteConstants()
    {
    }

    //
    // private helper methods for List<> creation
    //
    @SafeVarargs
    private static <T> List<T> asUnmodifiableList(T... listItems)
    {
        return Collections.unmodifiableList(Arrays.asList(listItems));
    }

    private static List<Subject> createSubjects(String... subjectStrings)
    {
        List<Subject> subjects = new ArrayList<>();

        for (String s : subjectStrings)
            subjects.add(new Subject(s));

        return subjects;
    }

    private static List<WebLink> createRelatedWebLinks(String... urls)
    {

        List<WebLink> webLinks = new ArrayList<>();

        for (String u : urls) {
            WebLink webLink = new WebLink(u);
            webLink.setType(WebLinkType.Related);
            webLinks.add(webLink);
        }

        return webLinks;

    }
}
