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
import java.util.List;

import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.utils.data.HttpRequester;

/**
 * Non-instantiable utility class for downloading the JSON responses for data
 * type and time series metadata, and also the actual time series datasets.
 *
 * @author Ingo Thomsen
 */
public final class OceanTeaDownloader
{

    private static final HttpRequester HTTP_REQUESTER = new HttpRequester();

    /**
     * private constructor to enforce non-instantiability
     */
    private OceanTeaDownloader()
    {
    }

    /**
     * Retrieve a list of {@linkplain TimeSeries} objects describing the metadata
     * for each time series dataset.
     *
     * @return an array of time series objects
     */
    public static List<TimeSeries> getAllTimeSeries()
    {
        AllTimeSeriesResponse allTimeSeriesResponse = HTTP_REQUESTER
                                                      .getObjectFromUrl(OceanTeaTimeSeriesDownloaderConstants.TIMESERIES_URL, AllTimeSeriesResponse.class);

        AllDataTypesResponse allDatatypesResponse = HTTP_REQUESTER
                                                    .getObjectFromUrl(OceanTeaTimeSeriesDownloaderConstants.DATATYPES_URL, AllDataTypesResponse.class);

        return JsonResponsesMerger.getAllTimeSeries(allTimeSeriesResponse, allDatatypesResponse);
    }

    /**
     * Download an actual dataset.
     *
     * @param url
     *            the download URL of the time series dataset.
     * @param referenceInstant
     *            the associated reference {@linkplain Instant}
     * @return a {@linkplain TimeSeriesDataset} object
     */
    public static TimeSeriesDataset getTimeSeriesDataset(String url, Instant referenceInstant)
    {
        TimeSeriesDatasetResponse timeSeriesDatasetResponse = HTTP_REQUESTER.getObjectFromUrl(url,
                                                              TimeSeriesDatasetResponse.class);

        return new TimeSeriesDataset(timeSeriesDatasetResponse, referenceInstant);
    }
}
