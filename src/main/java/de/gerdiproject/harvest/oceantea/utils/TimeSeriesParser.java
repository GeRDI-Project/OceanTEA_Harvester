/**
 * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de) Licensed under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.gerdiproject.harvest.oceantea.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.json.datacite.DateRange;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.extension.generic.ResearchData;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;
import de.gerdiproject.json.geo.GeoJson;
import de.gerdiproject.json.geo.Point;

/**
 * A Parser for creating elements for a (GeRDI) DataCite document from a
 * {@linkplain TimeSeries} object.
 *
 * @author Ingo Thomsen
 */
public class TimeSeriesParser
{
    private TimeSeries        timeSeries;
    private TimeSeriesDataset timeSeriesDataset;


    /**
     * Set up an {@linkplain TimeSeries} object for parsing by downloading the
     * corresponding {@linkplain TimeSeriesDataset}.
     *
     * @param time series a {@linkplain TimeSeries} object
     */
    public void setTimeSeries(TimeSeries timeSeries)
    {
        this.timeSeries = timeSeries;

    }


    /**
     * Sets up the corresponding {@linkplain TimeSeriesDataset} to the current
     * {@linkplain TimeSeries}.
     *
     * @param timeSeriesDatasetResponse a server response to a timeseries request
     */
    public void setTimeSeriesDataset(TimeSeriesDatasetResponse timeSeriesDatasetResponse)
    {
        this.timeSeriesDataset = new TimeSeriesDataset(timeSeriesDatasetResponse, timeSeries.getReferenceInstant());
    }


    /**
     * Assemble the URL for downloading the JSON representation.
     *
     * @return URL string
     */
    public String getDownloadUrl()
    {
        return String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                             OceanTeaTimeSeriesDownloaderConstants.DATASET_DOWNLOAD_URL,
                             timeSeries.getTimeSeriesType(),
                             timeSeries.getStation(),
                             timeSeries.getDataType(),
                             timeSeries.getDepth());
    }


    /**
     * Assemble the DataCite description of the ResearchData for the download URL of
     * the time series dataset.
     *
     * @return list of {@linkplain ResearchData}
     */
    public List<ResearchData> getResearchDataList()
    {
        String label = String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                     OceanTeaTimeSeriesDataCiteConstants.REASEARCH_DATA_LABEL,
                                     timeSeries.getDataTypePrintName(),
                                     timeSeries.getDepth(),
                                     timeSeries.getRegionPrintName(),
                                     timeSeries.getDevice());

        ResearchData researchData = new ResearchData(getDownloadUrl(), label);
        researchData.setType(OceanTeaTimeSeriesDataCiteConstants.JSON_MIME_MEDIA_TYPE);

        return Arrays.asList(researchData);
    }


    /**
     * The year of the publication (in OceanTEA) is not available, so the time of
     * the measurement is as close as it gets.
     *
     * @return year of publication (4 digits)
     */
    public int getPublicationYear()
    {
        Date date = timeSeries.getReferenceDate();
        SimpleDateFormat df = new SimpleDateFormat(
            OceanTeaTimeSeriesDataCiteConstants.PUBLICATION_YEAR_SIMPLE_DATE_FORMAT_STRING);

        return Integer.parseInt(df.format(date));
    }


    /**
     * Get the list of strings describing the subjects
     *
     * @return List of strings describing the subjects
     */
    public List<Subject> getSubjectsStrings()
    {
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(new Subject(OceanTeaTimeSeriesDataCiteConstants.SUBJECT_MOLAB_PREFIX + timeSeries.getDevice()));
        subjectList.add(new Subject(timeSeries.getDataTypePrintName()));
        subjectList.add(new Subject(timeSeries.getStation()));
        subjectList.add(new Subject(timeSeries.getRegionPrintName()));

        return subjectList;
    }


    /**
     * Get DataCite description - using a template string
     *
     * @return {@linkplain Description} object
     */
    public List<Description> getDescription()
    {
        Point point = timeSeries.getGeoLocationPoint();
        String geoLocationString = String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                                 OceanTeaTimeSeriesDataCiteConstants.GEO_LOCATION_AS_STRING,
                                                 point.getLongitude(),
                                                 point.getLatitude());

        String descriptionText = String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                               OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION,
                                               timeSeries.getDataTypePrintName(),
                                               timeSeriesDataset.getStartInstant(),
                                               timeSeriesDataset.getStopInstant(),
                                               timeSeriesDataset.getNumberOfValues(),
                                               timeSeries.getReferenceInstant(),
                                               timeSeries.getRegionPrintName(),
                                               geoLocationString,
                                               timeSeries.getDepth());

        if (timeSeriesDataset.getNumberOfMissingValues() > 0)
            descriptionText += String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                             OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION_MISSING_VALUES_SUFFIX,
                                             timeSeriesDataset.getNumberOfMissingValues());

        if (!timeSeries.getDataTypeUnit().isEmpty())
            descriptionText += String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                             OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION_MEASUREMENT_UNIT_SUFFIX,
                                             timeSeries.getDataTypeUnit());

        return Arrays.asList(new Description(descriptionText, DescriptionType.Abstract,
                                             OceanTeaTimeSeriesDataCiteConstants.LANG));
    }


    /**
     * Get DataCite title.
     *
     * @return {@linkplain Title} object
     */
    public Title getMainTitle()
    {
        String titleText = String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                         OceanTeaTimeSeriesDataCiteConstants.MAIN_DOCUMENT_TITLE,
                                         timeSeries.getDataTypePrintName(),
                                         timeSeries.getDepth(),
                                         timeSeries.getRegionPrintName());

        Title title = new Title(titleText);
        title.setLang(OceanTeaTimeSeriesDataCiteConstants.LANG);

        return title;
    }


    /**
     * The WebLinks consist only of the ViewURL, which has a varying title, but the
     * actual URL is always the same, because it is not possible to control the Demo
     * using URL parameters.
     *
     * @return list of WebLinks
     */
    public List<WebLink> getWebLinks()
    {
        WebLink webLink = new WebLink(OceanTeaTimeSeriesDataCiteConstants.VIEW_URL);
        webLink.setName(getMainTitle().getValue());
        webLink.setType(WebLinkType.ViewURL);

        return Arrays.asList(webLink);
    }


    /**
     * Return the one GeoLocation associated with the measurements including
     * mentioning of the region name.
     *
     * @return list containing one {@linkplain GeoLocation} object
     */
    public List<GeoLocation> getGeoLocations()
    {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setPoint(new GeoJson(timeSeries.getGeoLocationPoint()));
        geoLocation.setPlace(String.format(OceanTeaTimeSeriesDataCiteConstants.FORMATTING_LOCALE,
                                           OceanTeaTimeSeriesDataCiteConstants.GEOLOCATION_PLACE_DESCRIPTION,
                                           timeSeries.getRegionPrintName()));

        return Arrays.asList(geoLocation);
    }


    /**
     * Return the DataCite dates, which here is the {@linkplain DateRange} of the
     * time series data set.
     *
     * @return list containing one {@linkplain AbstractDate}.
     */
    public List<AbstractDate> getDates()
    {
        long epochMilliSince = timeSeriesDataset.getStartInstant().getEpochSecond() * 1000;
        long epochMilliUntil = timeSeriesDataset.getStopInstant().getEpochSecond() * 1000;

        DateRange dateRange = new DateRange(epochMilliSince, epochMilliUntil, DateType.Created);

        return Arrays.asList(dateRange);
    }
}
