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

import java.time.Instant;
import java.util.Date;

import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.json.geo.Point;

/**
 * This class represents the joint metadata collected from
 * {@linkplain AllTimeSeriesResponse} and {@linkplain AllDataTypesResponse}.
 *
 * @author Ingo Thomsen
 */
public final class TimeSeries
{

    private String region;
    private String regionPrintName;
    private String device;
    private String station;
    private String dataType;
    private String dataTypePrintName;
    private String dataTypeUnit;

    // Using slightly more descriptive field names here and Instant instead of Date
    private String timeSeriesType;
    private Instant instant;

    // geolocation point combining longitude, latitude AND depth
    Point geoLocationPoint;

    /**
     * Get the depth of the measurement, which is the negative elevation value of
     * the geolocation {@linkplain Point}}.
     *
     * @return depth of measurement
     */
    public double getDepth()
    {
        return -1 * geoLocationPoint.getElevation();
    }

    /**
     * Set the depth of the measurement point. It is stored as a negative elevation
     * value of the geolocation {@linkplain Point}}.
     *
     * @param depth
     *            a positive double value for the depth (in m)
     */
    public void setDepth(double depth)
    {
        geoLocationPoint.setElevation(-1 * depth);
    }

    //
    // Setter and Getter
    //

    public Point getGeoLocationPoint()
    {
        return geoLocationPoint;
    }

    public void setGeoLocationPoint(Point geoLocationPoint)
    {
        this.geoLocationPoint = geoLocationPoint;
    }

    public String getTimeSeriesType()
    {
        return timeSeriesType;
    }

    public void setTimeSeriesType(String timeSeriesType)
    {
        this.timeSeriesType = timeSeriesType;
    }

    public double getLatitude()
    {
        return geoLocationPoint.getLatitude();
    }

    public void setLatitude(double latitude)
    {
        this.geoLocationPoint.setLatitude(latitude);
    }

    public double getLongitude()
    {
        return geoLocationPoint.getLongitude();
    }

    public void setLongitude(double longitude)
    {
        this.geoLocationPoint.setLongitude(longitude);
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String value)
    {
        this.region = value;
    }

    public String getRegionPrintName()
    {
        return regionPrintName;
    }

    public void setRegionPrintName(String value)
    {
        this.regionPrintName = value;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String value)
    {
        this.device = value;
    }

    public String getStation()
    {
        return station;
    }

    public void setStation(String value)
    {
        this.station = value;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String value)
    {
        this.dataType = value;
    }

    public String getDataTypePrintName()
    {
        return dataTypePrintName;
    }

    public void setDataTypePrintName(String dataTypePrintName)
    {
        this.dataTypePrintName = dataTypePrintName;
    }

    public String getDataTypeUnit()
    {
        return dataTypeUnit;
    }

    public void setDataTypeUnit(String dataTypeUnit)
    {
        this.dataTypeUnit = dataTypeUnit;
    }

    public Date getReferenceDate()
    {
        return Date.from(instant);
    }

    public Instant getReferenceInstant()
    {
        return instant;
    }

    public void setReferenceInstant(Instant instant)
    {
        this.instant = instant;
    }

}