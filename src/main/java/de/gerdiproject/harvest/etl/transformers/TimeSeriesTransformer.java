/*
 *  Copyright © 2018 Robin Weiss (http://www.gerdi-project.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
package de.gerdiproject.harvest.etl.transformers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import de.gerdiproject.harvest.etls.transformers.AbstractIteratorTransformer;
import de.gerdiproject.harvest.etls.transformers.TransformerException;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesParser;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.extension.WebLink;

/**
 * This {@linkplain AbstractIteratorTransformer} implementation transforms OceanTea
 * {@linkplain TimeSeries} to {@linkplain DataCiteJson} documents.
 *
 * @author Ingo Thomsen, Robin Weiss
 */
public class TimeSeriesTransformer extends AbstractIteratorTransformer<TimeSeries, DataCiteJson>
{
    private final HttpRequester httpRequester = new HttpRequester(new Gson(), StandardCharsets.UTF_8);

    /**
     *  Parser to harvest non-constant information about time series datasets
     */
    private final TimeSeriesParser timeSeriesParser = new TimeSeriesParser();


    @Override
    protected DataCiteJson transformElement(TimeSeries timeSeries) throws TransformerException
    {
        // specify the TimeSeries objects for parsing
        timeSeriesParser.setTimeSeries(timeSeries);
        timeSeriesParser.setTimeSeriesDataset(
            httpRequester.getObjectFromUrl(
                timeSeriesParser.getDownloadUrl(),
                TimeSeriesDatasetResponse.class)
        );

        // create the document
        final DataCiteJson document = new DataCiteJson(timeSeries.getIdentifier());

        // derived from constants
        document.setResourceType(OceanTeaTimeSeriesDataCiteConstants.RESOURCE_TYPE);
        document.setPublisher(OceanTeaTimeSeriesDataCiteConstants.PROVIDER);
        document.setRepositoryIdentifier(OceanTeaTimeSeriesDataCiteConstants.REPOSITORY_ID);
        document.setCreators(OceanTeaTimeSeriesDataCiteConstants.CREATORS);
        document.setContributors(OceanTeaTimeSeriesDataCiteConstants.CONTRIBUTORS);
        document.setResearchDisciplines(OceanTeaTimeSeriesDataCiteConstants.DISCIPLINES);
        document.setFormats(OceanTeaTimeSeriesDataCiteConstants.FORMATS);

        // derived from both constants and the harvested entry

        // Subjects
        List<Subject> subjects = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.SUBJECTS);
        subjects.addAll(timeSeriesParser.getSubjectsStrings());
        document.setSubjects(subjects);

        // Descriptions
        List<Description> descriptions = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTIONS);
        descriptions.addAll(timeSeriesParser.getDescription());
        document.setDescriptions(descriptions);

        // WebLinks
        List<WebLink> webLinks = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.WEB_LINKS);
        webLinks.addAll(timeSeriesParser.getWebLinks());
        document.setWebLinks(webLinks);

        // derived exclusively from the harvested entry
        document.setResearchDataList(timeSeriesParser.getResearchDataList());
        document.setPublicationYear(timeSeriesParser.getPublicationYear());
        document.setTitles(Arrays.asList(timeSeriesParser.getMainTitle()));
        document.setGeoLocations(timeSeriesParser.getGeoLocations());
        document.setDates(timeSeriesParser.getDates());

        return document;
    }

}
