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
import java.util.ArrayList;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.DataTypeResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesResponse;
import de.gerdiproject.json.geo.Point;

/**
 *
 * Non-instantiable utility class for merging a {@linkplain TimeSeriesResponse} and a
 * {@linkplain DataTypeResponse}.
 *
 * @author Ingo Thomsen
 */
public final class JSONResponsesMerger
{

    /**
     * private constructor to enforce non-instantiability
     */
    private JSONResponsesMerger()
    {
    }

    /**
     * This method combines the responses on the time series and the data types and
     * returns a list of {@linkplain TimeSeries} objects.
     *
     * @param allTimeSeriesResponse
     *            an {@linkplain AllTimeSeriesResponse} object
     * @param allDatatypesResponse
     *            an {@linkplain AllDataTypesResponse} object
     * @return list of {@linkplain TimeSeries} objects
     */
    public static List<TimeSeries> getAllTimeSeries(AllTimeSeriesResponse allTimeSeriesResponse,
                                                    AllDataTypesResponse allDatatypesResponse)
    {
        List<TimeSeries> result = new ArrayList<>();

        for (TimeSeriesResponse response : allTimeSeriesResponse.getAllTimeSeriesResponses()) {

            // skipping non-univariant time series data
            if (response.getTsType().equalsIgnoreCase(OceanTeaTimeSeriesDownloaderConstants.NON_UNIVARIANT_TIME_SERIES))
                continue;

            TimeSeries timeSeries = new TimeSeries();

            //
            // fields with a direct mapping
            //
            timeSeries.setRegion(response.getRegion());
            timeSeries.setRegionPrintName(response.getRegionPrintName());
            timeSeries.setDevice(response.getDevice());
            timeSeries.setTimeSeriesType(response.getTsType());
            timeSeries.setStation(response.getStation());
            timeSeries.setDataType(response.getDataType());

            // creating the geolocation
            timeSeries.setGeoLocationPoint(new Point(response.getLon(), response.getLat(), response.getDepth() * -1));

            // converting reference ISO 8601 date (example: 2012-06-01T00:00:01Z) to Instant
            timeSeries.setReferenceInstant(Instant.parse(response.getTReference()));

            //
            // enrich with data type information
            //
            DataTypeResponse datatypeResponse = allDatatypesResponse
                                                .getDatatypeResponseByName(timeSeries.getDataType());
            timeSeries.setDataTypePrintName(datatypeResponse.getPrintName());
            timeSeries.setDataTypeUnit(datatypeResponse.getUnit());

            result.add(timeSeries);
        }

        return result;
    }
}
