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
package de.gerdiproject.harvest.oceantea.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDownloaderConstants;
import de.gerdiproject.json.datacite.DateRange;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.extension.ResearchData;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;
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

    private TimeSeries timeSeries;
    private TimeSeriesDataset timeSeriesDataset;

    /**
     * Set up an {@linkplain TimeSeries} object for parsing by downloading the
     * corresponding {@linkplain TimeSeriesDataset}.
     *
     * @param time
     *            series a {@linkplain TimeSeries} object
     */
    public void setTimeSeries(TimeSeries timeSeries)
    {
        this.timeSeries = timeSeries;
        this.timeSeriesDataset = OceanTeaDownloader.getTimeSeriesDataset(getDownloadUrl(),
                                                                         timeSeries.getReferenceInstant());
    }

    /**
     * Assemble the URL for downloading the JSON representation.
     *
     * @return URL string
     */
    public String getDownloadUrl()
    {

        // For the link an integer depth must be inserted without ".0"
        String depthString = Integer.toString((int) timeSeries.getDepth());
        String url = String.format(OceanTeaTimeSeriesDownloaderConstants.DATASET_DOWNLOAD_URL,
                                   timeSeries.getTimeSeriesType(), timeSeries.getStation(), timeSeries.getDataType(), depthString);

        return url;
    }

    /**
     * Assemble the DataCite description of the ResearchData for the download URL of
     * the time series dataset.
     *
     * @return list of {@linkplain ResearchData}
     */
    public List<ResearchData> getResearchDataList()
    {

        String label = String.format(OceanTeaTimeSeriesDataCiteConstants.REASEARCH_DATA_LABEL,
                                     timeSeries.getDataTypePrintName(), timeSeries.getDepth(), timeSeries.getRegionPrintName(),
                                     timeSeries.getDevice());

        ResearchData researchData = new ResearchData(getDownloadUrl(), label);
        researchData.setType("application/json");

        return Arrays.asList(researchData);

    }

    /**
     * The year of the publication (in OceanTEA) is not available, so the time of
     * the measurement is as close as it gets.
     *
     * @return year of publication (4 digits)
     */
    public short getPublicationYear()
    {

        Date date = timeSeries.getReferenceDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");

        return Short.parseShort(df.format(date));
    }

    /**
     * Get the list of strings describing the subjects
     *
     * @return List of strings describing the subjects
     */
    public List<Subject> getSubjectsStrings()
    {

        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(new Subject("MoLab " + timeSeries.getDevice()));
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
        String geoLocationString = "(" + point.getLongitude() + ";" + point.getLatitude() + ")";

        String descriptionText = String.format(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION,
                                               timeSeries.getDataTypePrintName(), timeSeriesDataset.getStartInstant(),
                                               timeSeriesDataset.getStopInstant(), timeSeriesDataset.getValuesMean(), timeSeries.getDataTypeUnit(),
                                               timeSeriesDataset.getNumberOfValues(), timeSeries.getReferenceInstant(),
                                               timeSeries.getRegionPrintName(), geoLocationString, timeSeries.getDepth());

        if (timeSeriesDataset.getMissingValues() > 0)
            descriptionText += " " + timeSeriesDataset.getMissingValues() + "measurements points were missing ('NA').";




        return Arrays.asList(
                   new Description(descriptionText, DescriptionType.Abstract, OceanTeaTimeSeriesDataCiteConstants.LANG));
    }

    /**
     * Get DataCite title.
     *
     * @return {@linkplain Title} object
     */
    public Title getMainTitle()
    {

        String titleText = String.format(OceanTeaTimeSeriesDataCiteConstants.MAIN_DOCUMENT_TITLE,
                                         timeSeries.getDataTypePrintName(), timeSeries.getDepth(), timeSeries.getRegionPrintName());

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
        geoLocation.setPlace("measurement region of " + timeSeries.getRegionPrintName());

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
