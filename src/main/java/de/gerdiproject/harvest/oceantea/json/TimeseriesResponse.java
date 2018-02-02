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
package de.gerdiproject.harvest.oceantea.json;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a JSON object that is part of an
 * {@linkplain AllTimeseriesResponse}.
 *
 * @author Ingo Thomsen
 */
public final class TimeseriesResponse {
	private String region;
	private String regionPrintName;
	private String device;
	private String tsType;
	private String station;
	private String dataType;
	private double depth;
	private double lat;
	private double lon;

	@SerializedName("t_reference")
	private String tReference;

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

	public String getTsType() {
		return tsType;
	}

	public void setTsType(String value) {
		this.tsType = value;
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

	public double getLat() {
		return lat;
	}

	public void setLat(double value) {
		this.lat = value;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double value) {
		this.lon = value;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double value) {
		this.depth = value;
	}

	public String getTReference() {
		return tReference;
	}

	public void setTReference(String value) {
		this.tReference = value;
	}
}