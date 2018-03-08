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
import java.util.Collections;
import java.util.List;

import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;

/**
 * This class represents information about a time series data set, based upon
 * the raw data provided by a {@linkplain TimeSeriesDatasetResponse}:
 *
 * Three {@link Instant)s describing the start and stop of the measurements and
 * the reference for the time offsets. Also the total number and the number of
 * missing values are provided.
 *
 * @author Ingo Thomsen
 */
public final class TimeSeriesDataset
{

    private final Instant referenceInstant;
    private final Instant startInstant;
    private final Instant stopInstant;

    private final int numberOfValues;
    private int numberOfMissingValues = 0;

    /**
     * Constructor using a {@linkplain TimeSeriesDatasetResponse} and its reference
     * {@linkplain Instant}.
     *
     * @param timeSeriesDatasetResponse
     *            {@linkplain TimeSeriesDatasetResponse} containing the raw values
     * @param referenceInstant
     *            reference {@linkplain Instant} for the time offsets
     */
    public TimeSeriesDataset(TimeSeriesDatasetResponse timeSeriesDatasetResponse, Instant referenceInstant)
    {
        this.referenceInstant = referenceInstant;

        // extract the values and time offsets in the TimeSeriesDatasetResponse
        List<Integer> timeOffsets = new ArrayList<>();

        for (List<String> pairOfTimeOffsetAndValue : timeSeriesDatasetResponse.getListOfPairsOfTimeOffsetAndValue())
            try {
                timeOffsets.add(Integer.parseInt(pairOfTimeOffsetAndValue.get(0)));

            } catch (NumberFormatException e) {
                numberOfMissingValues++;
            }

        // extract info from values and time offsets
        startInstant = Instant.ofEpochSecond(referenceInstant.getEpochSecond() + Collections.min(timeOffsets));
        stopInstant = Instant.ofEpochSecond(referenceInstant.getEpochSecond() + Collections.max(timeOffsets));
        numberOfValues = timeOffsets.size();
    }


    //
    // Getters
    //

    public Instant getReferenceInstant()
    {
        return referenceInstant;
    }

    public Instant getStartInstant()
    {
        return startInstant;
    }

    public Instant getStopInstant()
    {
        return stopInstant;
    }

    public int getNumberOfValues()
    {
        return numberOfValues;
    }

    public int getNumberOfMissingValues()
    {
        return numberOfMissingValues;
    }

}