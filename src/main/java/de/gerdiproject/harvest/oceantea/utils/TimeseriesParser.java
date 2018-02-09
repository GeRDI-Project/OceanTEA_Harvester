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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeseriesDownloaderConstants;
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
 * A Parser for creating elements for (GeRDI) a DataCite document from a
 * {@linkplain Timeseries} object.
 *
 * @author Ingo Thomsen
 */
public class TimeseriesParser {

	private Timeseries ts;
	private TimeseriesDataset tsd;

	/**
	 * Set up a Timeseries for parsing by downloading the corresponding
	 * {@linkplain TimeseriesDataset}.
	 * 
	 * @param timeseries
	 *            a {@linkplain Timeseries} object
	 */
	public void setTimeseries(Timeseries timeseries) {
		this.ts = timeseries;
		this.tsd = OceanTeaDownloader.getTimeseriesDataset(getDownloadUrl(), ts.getReferenceInstant());
	}

	/**
	 * Assemble the URL for downloading the JSON representation.
	 *
	 * @return URL string
	 */
	public String getDownloadUrl() {

		// For the link an integer depth must be inserted without ".0"
		Double depth = ts.getDepth();
		String depth_string = (depth % 1) == 0 ? Integer.toString(depth.intValue()) : depth.toString();

		String url = String.format(OceanTeaTimeseriesDownloaderConstants.DATASET_DOWNLOAD_URL, ts.getTimeseriesType(),
				ts.getStation(), ts.getDataType(), depth_string);

		return url;
	}

	/**
	 * Assemble the DataCite description of the ResearchData for the download URL of
	 * the timeseries dataset.
	 *
	 * @return list of {@linkplain ResearchData}
	 */
	public List<ResearchData> getResearchDataList() {

		String label = String.format(OceanTeaTimeSeriesDataCiteConstants.REASEARCH_DATA_LABEL,
				ts.getDataTypePrintName(), ts.getDepth(), ts.getRegionPrintName(), ts.getDevice());

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
	public short getPublicationYear() {

		Date date = ts.getReferenceDate();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");

		return Short.parseShort(df.format(date));
	}

	/**
	 * Get the list of strings describing the subjects
	 *
	 * @return List of strings describing the subjects
	 */
	public List<Subject> getSubjectsStrings() {

		List<Subject> subjectList = new ArrayList<>();
		subjectList.add(new Subject("MoLab " + ts.getDevice()));
		subjectList.add(new Subject(ts.getDataTypePrintName()));
		subjectList.add(new Subject(ts.getStation()));
		subjectList.add(new Subject(ts.getRegionPrintName()));

		return subjectList;
	}

	/**
	 * Get DataCite description - using a template string
	 *
	 * @return {@linkplain Description} object
	 */
	public List<Description> getDescription() {

		Point point = ts.getGeoLocationPoint();
		String geoLocationString = "(" + point.getLongitude() + ";" + point.getLatitude() + ")";

		String descriptionText = String.format(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION,
				ts.getDataTypePrintName(), tsd.getStartInstant(), tsd.getStopInstant(), tsd.getValuesMean(),
				ts.getDataTypeUnit(), tsd.getNumberOfValues(), ts.getReferenceInstant(), ts.getRegionPrintName(),
				geoLocationString, ts.getDepth());

		if (tsd.getMissingValues() > 0)
			descriptionText += " " + tsd.getMissingValues() + "measurements points were missing ('NA').";

		return Arrays.asList(
				new Description(descriptionText, DescriptionType.Abstract, OceanTeaTimeSeriesDataCiteConstants.LANG));
	}

	/**
	 * Get DataCite title.
	 *
	 * @return {@linkplain Title} object
	 */
	public Title getMainTitle() {

		String titleText = String.format(OceanTeaTimeSeriesDataCiteConstants.MAIN_DOCUMENT_TITLE,
				ts.getDataTypePrintName(), ts.getDepth(), ts.getRegionPrintName());

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
	public List<WebLink> getWebLinks() {

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
	public List<GeoLocation> getGeoLocations() {

		GeoLocation geoLocation = new GeoLocation();
		geoLocation.setPoint(new GeoJson(ts.getGeoLocationPoint()));
		geoLocation.setPlace("measurement region of " + ts.getRegionPrintName());

		return Arrays.asList(geoLocation);
	}

	/**
	 * Return the DataCite dates, which here is the {@linkplain DateRange} of the
	 * timeseries data.
	 *
	 * @return list containing one {@linkplain AbstractDate}.
	 */
	public List<AbstractDate> getDates() {

		long epochMilliSince = tsd.getStartInstant().getEpochSecond() * 1000;
		long epochMilliUntil = tsd.getStopInstant().getEpochSecond() * 1000;

		DateRange dateRange = new DateRange(epochMilliSince, epochMilliUntil, DateType.Created);

		return Arrays.asList(dateRange);
	}

}
