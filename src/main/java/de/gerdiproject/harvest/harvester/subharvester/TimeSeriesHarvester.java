/**
, * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de)
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
package de.gerdiproject.harvest.harvester.subharvester;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.harvester.AbstractListHarvester;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.utils.OceanTeaDownloader;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesParser;
import de.gerdiproject.harvest.utils.HashGenerator;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.extension.WebLink;

/**
 * Harvester for OceanTEA time series data
 *
 * @author Ingo Thomsen
 */
public class TimeSeriesHarvester extends AbstractListHarvester<TimeSeries>
{

    // parser to harvest non-constant information about time series datasets
    private final TimeSeriesParser timeSeriesParser = new TimeSeriesParser();


    /**
     * Default constructor, naming the harvester and ensuring one document per
     * harvested entry
     */
    public TimeSeriesHarvester()
    {
        super("OceanTEA - Time Series", 1);
    }


    @Override
    protected Collection<TimeSeries> loadEntries()
    {
        return OceanTeaDownloader.getAllTimeSeries();
    }


    @Override
    protected String initHash() throws NoSuchAlgorithmException, NullPointerException
    {
        // OceanTea entries are not altered. If timeseries are modified, new ones were added
        // thus the number of documents suffices to check for changes
        return HashGenerator.instance().getShaHash(String.valueOf(entries.size()));
    }


    @Override
    protected List<IDocument> harvestEntry(TimeSeries timeSeries)
    {
        // specify the TimeSeries object for parsing
        timeSeriesParser.setTimeSeries(timeSeries);

        // create the document
        DataCiteJson document = new DataCiteJson(timeSeries.createIdentifier());

        //
        // derived from constants
        //
        document.setResourceType(OceanTeaTimeSeriesDataCiteConstants.RESOURCE_TYPE);
        document.setPublisher(OceanTeaTimeSeriesDataCiteConstants.PROVIDER);
        document.setRepositoryIdentifier(OceanTeaTimeSeriesDataCiteConstants.REPOSITORY_ID);
        document.setCreators(OceanTeaTimeSeriesDataCiteConstants.CREATORS);
        document.setContributors(OceanTeaTimeSeriesDataCiteConstants.CONTRIBUTORS);
        document.setResearchDisciplines(OceanTeaTimeSeriesDataCiteConstants.DISCIPLINES);
        document.setFormats(OceanTeaTimeSeriesDataCiteConstants.FORMATS);

        //
        // derived from both constants and the harvested entry
        //

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

        //
        // derived exclusively from the harvested entry
        //
        document.setResearchDataList(timeSeriesParser.getResearchDataList());
        document.setPublicationYear(timeSeriesParser.getPublicationYear());
        document.setTitles(Arrays.asList(timeSeriesParser.getMainTitle()));
        document.setGeoLocations(timeSeriesParser.getGeoLocations());
        document.setDates(timeSeriesParser.getDates());

        return Arrays.asList(document);
    }

}
