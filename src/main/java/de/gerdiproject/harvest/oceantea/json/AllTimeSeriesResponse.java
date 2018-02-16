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

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a JSON response to an "all time series" requests, for
 * example: http://maui.se.informatik.uni-kiel.de:9090/timeseries
 *
 * @author Ingo Thomsen
 */
public final class AllTimeSeriesResponse
{

    @SerializedName("timeseries")
    private List<TimeSeriesResponse> allTimeSeriesResponses;

    //
    // Getter and Setter
    //

    public List<TimeSeriesResponse> getAllTimeSeriesResponses()
    {
        return allTimeSeriesResponses;
    }

    public void setAllTimeSeriesResponses(List<TimeSeriesResponse> value)
    {
        this.allTimeSeriesResponses = value;
    }

}