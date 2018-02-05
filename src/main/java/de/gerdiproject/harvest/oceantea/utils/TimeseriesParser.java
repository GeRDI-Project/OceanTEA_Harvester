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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeseriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.AllDatatypesResponse;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.extension.ResearchData;
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

	public TimeseriesParser(Timeseries timeseries) {
		this.ts = timeseries;
	}

	public List<ResearchData> getResearchDataList() {
		
		// For the link an integer depth must be inserted without ".0"
		Double depth = ts.getDepth();
		String depth_string = (depth % 1) == 0 ? Integer.toString(depth.intValue()) : depth.toString();

		String url = String.format(OceanTeaTimeseriesDownloaderConstants.DATASET_DOWNLOAD_URL, ts.getTimeseriesType(),
				ts.getStation(), ts.getDataType(), depth_string);


		String label = String.format(OceanTeaTimeSeriesDataCiteConstants.REASEARCH_DATA_LABEL,
				ts.getDataTypePrintName(), depth_string, ts.getRegionPrintName(), ts.getDevice());

		ResearchData researchData = new ResearchData(url, label);
		researchData.setType("application/json");

		return Arrays.asList(researchData);

	}

	// the reference date is the only date information available
	public short getPublicationYear() {

		Date date = ts.getReferenceDate();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");

		return Short.parseShort(df.format(date));
	}

	public List<String> getSubjectsStrings() {

		return Arrays.asList("MoLab " + ts.getDevice(), "underwater measurement", ts.getDataTypePrintName(),
				ts.getRegionPrintName(), ts.getStation());
	}

	public Description getDescription() {

		Point point = ts.getGeoLocationPoint();
		String geoLocationString = "(" + point.getLongitude() + ";" + point.getLatitude() + ")";

		String descriptionText = String.format(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION,
				ts.getDataTypePrintName(), ts.getReferenceDate().toString(), ts.getRegionPrintName(), geoLocationString,
				ts.getDepth());

		return new Description(descriptionText, DescriptionType.Abstract, OceanTeaTimeSeriesDataCiteConstants.LANG);
	}

	public Title getMainTitle() {

		String titleText = String.format(OceanTeaTimeSeriesDataCiteConstants.MAIN_DOCUMENT_TITLE,
				ts.getDataTypePrintName(), ts.getDepth(), ts.getRegionPrintName());

		Title title = new Title(titleText);
		title.setLang(OceanTeaTimeSeriesDataCiteConstants.LANG);

		return title;
	}

	public List<GeoLocation> getGeoLocations() {

		GeoLocation geoLocation = new GeoLocation();
		geoLocation.setPoint(new GeoJson(ts.getGeoLocationPoint()));

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
