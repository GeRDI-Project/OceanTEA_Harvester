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
import java.util.ArrayList;
import java.util.List;

import de.gerdiproject.harvest.oceantea.json.AllDatatypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeseriesResponse;
import de.gerdiproject.harvest.oceantea.json.DatatypeResponse;
import de.gerdiproject.harvest.oceantea.json.TimeseriesResponse;
import de.gerdiproject.json.geo.Point;

/**
 *
 * A static class for merging a {@linkplain TimeseriesResponse} and a
 * {@linkplain DatatypeResponse}.
 *
 * @author Ingo Thomsen
 */
public class JsonResponsesMerger
{

    // static class (therefore private constructor)
    private JsonResponsesMerger()
    {
    }

    /**
     * This method combines the responses on the timeseries and the datatypes and
     * returns a list of {@linkplain Timeseries} objects.
     *
     * @param allTimeseriesResponse
     * @param allDatatypesResponse
     * @return list of {@linkplain Timeseries} objects
     */
    public static List<Timeseries> getAllTimeseries(AllTimeseriesResponse allTimeseriesResponse,
                                                    AllDatatypesResponse allDatatypesResponse)
    {

        List<Timeseries> result = new ArrayList<>();

        for (TimeseriesResponse response : allTimeseriesResponse.getAllTimeseriesResponses()) {

            // skipping non-timeseries data
            if (response.getTsType().equalsIgnoreCase("adcp"))
                continue;

            Timeseries timeseries = new Timeseries();

            //
            // fields with a direct mapping
            //
            timeseries.setRegion(response.getRegion());
            timeseries.setRegionPrintName(response.getRegionPrintName());
            timeseries.setDevice(response.getDevice());
            timeseries.setTimeseriesType(response.getTsType());
            timeseries.setStation(response.getStation());
            timeseries.setDataType(response.getDataType());

            // creating the geolocation
            timeseries.setGeoLocationPoint(new Point(response.getLon(), response.getLat(), response.getDepth() * -1));

            // converting reference ISO 8601 date (example: 2012-06-01T00:00:01Z) to Instant
            timeseries.setReferenceInstant(Instant.parse(response.getTReference()));

            //
            // enrich with data type information
            //
            DatatypeResponse datatypeResponse = allDatatypesResponse
                                                .getDatatypeResponseByName(timeseries.getDataType());
            timeseries.setDataTypePrintName(datatypeResponse.getPrintName());
            timeseries.setDataTypeUnit(datatypeResponse.getUnit());

            result.add(timeseries);
        }

        return result;

    }
}
