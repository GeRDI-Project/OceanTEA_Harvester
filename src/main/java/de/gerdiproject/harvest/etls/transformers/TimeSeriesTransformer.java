/**
 * Copyright © 2018 Ingo Thomsen, Robin Weiss (http://www.gerdi-project.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gerdiproject.harvest.etls.transformers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.gson.Gson;

import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesParser;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.datacite.DataCiteJson;

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
    public void init(AbstractETL<?, ?> etl)
    {
        // nothing to retrieve from the ETL
    }


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
        document.addCreators(OceanTeaTimeSeriesDataCiteConstants.CREATORS);
        document.addContributors(OceanTeaTimeSeriesDataCiteConstants.CONTRIBUTORS);
        document.addResearchDisciplines(OceanTeaTimeSeriesDataCiteConstants.DISCIPLINES);
        document.addFormats(OceanTeaTimeSeriesDataCiteConstants.FORMATS);
        document.addSubjects(OceanTeaTimeSeriesDataCiteConstants.SUBJECTS);
        document.addDescriptions(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTIONS);
        document.addWebLinks(OceanTeaTimeSeriesDataCiteConstants.WEB_LINKS);

        // derived from the harvested entry
        document.addSubjects(timeSeriesParser.getSubjectsStrings());
        document.addDescriptions(timeSeriesParser.getDescription());
        document.addWebLinks(timeSeriesParser.getWebLinks());
        document.addResearchData(timeSeriesParser.getResearchDataList());
        document.setPublicationYear(timeSeriesParser.getPublicationYear());
        document.addTitles(Arrays.asList(timeSeriesParser.getMainTitle()));
        document.addGeoLocations(timeSeriesParser.getGeoLocations());
        document.addDates(timeSeriesParser.getDates());

        return document;
    }


    @Override
    public void clear()
    {
        // nothing to clean up
    }
}
