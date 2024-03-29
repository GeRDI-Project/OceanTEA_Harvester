/**
 * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de)
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
package de.gerdiproject.harvest.oceantea.utils;

import java.time.Instant;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.DataTypeResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesResponse;
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
    private static final GeometryFactory GEO_FACTORY = new GeometryFactory();

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
    public TimeSeries(final int index, final TimeSeriesResponse timeSeriesData, final DataTypeResponse dataTypeInfo)
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
        final Coordinate pointCoordinate = new Coordinate(
            timeSeriesData.getLon(),
            timeSeriesData.getLat(),
            timeSeriesData.getDepth() * -1);

        setGeoLocationPoint(GEO_FACTORY.createPoint(pointCoordinate));

        // converting reference ISO 8601 date (example: 2012-06-01T00:00:01Z) to Instant
        setReferenceInstant(Instant.parse(timeSeriesData.getTReference()));

        // enrich with data type information
        setDataTypePrintName(dataTypeInfo.getPrintName());
        setDataTypeUnit(dataTypeInfo.getUnit());
    }


    /**
     * Get the latitude from the geolocation {@linkplain Point}}
     *
     * @return latitude from the geolocation {@linkplain Point}
     */
    public double getLatitude()
    {
        return geoLocationPoint.getY();
    }


    /**
     * Get the longitude from the geolocation {@linkplain Point}}
     *
     * @return longitude from the geolocation {@linkplain Point}}
     */
    public double getLongitude()
    {
        return geoLocationPoint.getX();
    }


    /**
     * Get the depth of the measurement, which is the negative elevation value of
     * the geolocation {@linkplain Point}}.
     *
     * @return depth of measurement: a positive value, which is the negative
     *         elevation value of the geolocation {@linkplain Point}
     */
    public double getDepth()
    {
        return -1.0 * geoLocationPoint.getCoordinate().z;
    }
}
