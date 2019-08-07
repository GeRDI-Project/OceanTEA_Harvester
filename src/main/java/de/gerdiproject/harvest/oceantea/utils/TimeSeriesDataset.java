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
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * This class represents information about a time series data set, based upon
 * the raw data provided by a {@linkplain TimeSeriesDatasetResponse}: Three
 * {@link Instant)s describing the start and stop of the measurements and the
 * reference for the time offsets. Also the total number and the number of
 * missing values are provided.
 *
 * @author Ingo Thomsen
 */
@Data
public final class TimeSeriesDataset
{
    @Setter(AccessLevel.NONE)
    private int numberOfMissingValues;

    private final int numberOfValues;
    private final Instant referenceInstant;
    private final Instant startInstant;
    private final Instant stopInstant;


    /**
     * Constructor using a {@linkplain TimeSeriesDatasetResponse} and its reference
     * {@linkplain Instant}.
     *
     * @param timeSeriesDatasetResponse {@linkplain TimeSeriesDatasetResponse}
     *            containing the raw values
     * @param referenceInstant reference {@linkplain Instant} for the time offsets
     */
    public TimeSeriesDataset(final TimeSeriesDatasetResponse timeSeriesDatasetResponse, final Instant referenceInstant)
    {
        this.referenceInstant = referenceInstant;

        // extract the values and time offsets in the TimeSeriesDatasetResponse
        final List<Integer> timeOffsets = new ArrayList<>();

        this.numberOfMissingValues = 0;

        for (final List<String> pairOfTimeOffsetAndValue : timeSeriesDatasetResponse.getListOfPairsOfTimeOffsetAndValue())
            try {
                timeOffsets.add(Integer.parseInt(pairOfTimeOffsetAndValue.get(0)));

            } catch (final NumberFormatException e) {
                this.numberOfMissingValues++;
            }

        // extract info from values and time offsets
        this.numberOfValues = timeOffsets.size();

        if (this.numberOfValues > 0) {
            this.startInstant = Instant.ofEpochSecond(referenceInstant.getEpochSecond() + Collections.min(timeOffsets));
            this.stopInstant = Instant.ofEpochSecond(referenceInstant.getEpochSecond() + Collections.max(timeOffsets));

        } else {
            this.startInstant = referenceInstant;
            this.stopInstant = referenceInstant;
        }
    }
}