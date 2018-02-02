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

import java.util.Date;

import de.gerdiproject.harvest.oceantea.json.AllDatatypesResponse;
import de.gerdiproject.json.geo.Point;

/**
 * This class represents the joint metadata collected from
 * {@linkplain AllTimeseriesResponse} and {@linkplain AllDatatypesResponse}.
 *
 * @author Ingo Thomsen
 */
public final class Timeseries {

	private String region;
	private String regionPrintName;
	private String device;
	private String station;
	private String dataType;
	private String dataTypePrintName;
	private String dataTypeUnit;

	// Using slightly more descriptive field names here.
	private String timeseriesType;
	private Date referenceDate;

	// geolocation point combining longitude, latitude AND depth
	Point geoLocationPoint;

	public Double getDepth() {
		return -1 * geoLocationPoint.getElevation();
	}

	public void setDepth(Double depth) {
		geoLocationPoint.setElevation(-1 * depth);
	}

	//
	// Setter and Getters
	//
	public Point getGeoLocationPoint() {
		return geoLocationPoint;
	}

	public void setGeoLocationPoint(Point geoLocationPoint) {
		this.geoLocationPoint = geoLocationPoint;
	}

	public String getTimeseriesType() {
		return timeseriesType;
	}

	public void setTimeseriesType(String timeseriesType) {
		this.timeseriesType = timeseriesType;
	}

	public double getLatitude() {
		return geoLocationPoint.getLatitude();
	}

	public void setLatitude(double latitude) {
		this.geoLocationPoint.setLatitude(latitude);
	}

	public double getLongitude() {
		return geoLocationPoint.getLongitude();
	}

	public void setLongitude(double longitude) {
		this.geoLocationPoint.setLongitude(longitude);
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String value) {
		this.region = value;
	}

	public String getRegionPrintName() {
		return regionPrintName;
	}

	public void setRegionPrintName(String value) {
		this.regionPrintName = value;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String value) {
		this.device = value;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String value) {
		this.station = value;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String value) {
		this.dataType = value;
	}

	public String getDataTypePrintName() {
		return dataTypePrintName;
	}

	public void setDataTypePrintName(String dataTypePrintName) {
		this.dataTypePrintName = dataTypePrintName;
	}

	public String getDataTypeUnit() {
		return dataTypeUnit;
	}

	public void setDataTypeUnit(String dataTypeUnit) {
		this.dataTypeUnit = dataTypeUnit;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

}