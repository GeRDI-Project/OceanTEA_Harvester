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
import java.util.Collection;
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeseriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.AllDatatypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeseriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeseriesDatasetResponse;
import de.gerdiproject.harvest.utils.data.HttpRequester;

/**
 * A static class for downloading the JSON responses for datatype and timeseries
 * metadata and the actual timeseries datasets
 *
 * @author Ingo Thomsen
 */
public class OceanTeaDownloader
{

    static private final HttpRequester httpRequester = new HttpRequester();

    // static class (therefore private constructor)
    private OceanTeaDownloader()
    {
    }

    /**
     * Retrieve a list of {@linkplain Timeseries} objects describing the metadata
     * for each timeseries data set.
     *
     * @return an array of timeseries objects
     */
    static public List<Timeseries> getAllTimeseries()
    {

        String baseUrl = OceanTeaTimeSeriesDataCiteConstants.BASE_URL;

        String url1 = baseUrl + OceanTeaTimeseriesDownloaderConstants.TIMESERIES_URL;
        AllTimeseriesResponse allTimeseriesResponse = httpRequester.getObjectFromUrl(url1, AllTimeseriesResponse.class);

        String url2 = baseUrl + OceanTeaTimeseriesDownloaderConstants.DATATYPES_URL;
        AllDatatypesResponse allDatatypesResponse = httpRequester.getObjectFromUrl(url2, AllDatatypesResponse.class);

        return JsonResponsesMerger.getAllTimeseries(allTimeseriesResponse, allDatatypesResponse);
    }

    /**
     * Download an actual dataset.
     *
     * @param download URL of the timeseries dataset.
     * @param the associated reference {@linkplain Instant}
     * @return a {@linkplain TimeseriesDataset} object
     */
    static public TimeseriesDataset getTimeseriesDataset(String url, Instant referenceInstant)
    {

        TimeseriesDatasetResponse timeseriesDatasetResponse = httpRequester.getObjectFromUrl(url,
                                                              TimeseriesDatasetResponse.class);

        return new TimeseriesDataset(timeseriesDatasetResponse, referenceInstant);
    }
}
