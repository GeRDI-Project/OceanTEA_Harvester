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

import java.time.Instant;
import java.util.Date;

import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.DataTypeResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesResponse;
import de.gerdiproject.json.geo.Point;
import lombok.Data;

/**
 * This class represents the joint metadata collected from
 * {@linkplain AllTimeSeriesResponse} and {@linkplain AllDataTypesResponse}.
 *
 * @author Ingo Thomsen
 */
@Data
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
    private String  timeSeriesType;
    private Instant referenceInstant;

    // geolocation point combining longitude, latitude AND depth
    private Point geoLocationPoint;

    private final String identifier;


    /**
     * Constructor that sets fields from JSON responses.
     *
     * @param index the unique index of the timeseries
     * @param timeSeriesData part of a JSON response to a timeseries request
     * @param dataTypeInfo the corresponding data type information
     */
    public TimeSeries(int index, TimeSeriesResponse timeSeriesData, DataTypeResponse dataTypeInfo)
    {
        this.identifier = getClass().getSimpleName() + index;

        // fields with a direct mapping
        setRegion(timeSeriesData.getRegion());
        setRegionPrintName(timeSeriesData.getRegionPrintName());
        setDevice(timeSeriesData.getDevice());
        setTimeSeriesType(timeSeriesData.getTsType());
        setStation(timeSeriesData.getStation());
        setDataType(timeSeriesData.getDataType());

        // creating the geolocation
        setGeoLocationPoint(new Point(timeSeriesData.getLon(), timeSeriesData.getLat(), timeSeriesData
                                      .getDepth() * -1));

        // converting reference ISO 8601 date (example: 2012-06-01T00:00:01Z) to Instant
        setReferenceInstant(Instant.parse(timeSeriesData.getTReference()));

        // enrich with data type information
        setDataTypePrintName(dataTypeInfo.getPrintName());
        setDataTypeUnit(dataTypeInfo.getUnit());
    }


    /**
     * Get the latitude (from the geolocation {@linkplain Point}})
     *
     * @return latitude
     */
    public double getLatitude()
    {
        return geoLocationPoint.getLatitude();
    }


    /**
     * Get the longitude (from the geolocation {@linkplain Point}})
     *
     * @return longitude
     */
    public double getLongitude()
    {
        return geoLocationPoint.getLongitude();
    }


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
     * Get the reference {@linkplain Instant} as {@linkplain Date}.
     *
     * @return reference {@linkplain Date}
     */
    public Date getReferenceDate()
    {
        return Date.from(referenceInstant);
    }
}
