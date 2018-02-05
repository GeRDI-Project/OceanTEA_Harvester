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
import java.util.Collections;
import java.util.List;

import de.gerdiproject.harvest.oceantea.json.TimeseriesDatasetResponse;

/**
 * This class represents the raw data for a timeseries dataset: A list of time
 * offsets and the associated values.
 *
 * It also offers the calculated start and stop Instant plus the mean of the
 * values
 *
 * @author Ingo Thomsen
 */
public final class TimeseriesDataset {

	private List<Integer> timeOffsets = new ArrayList<>();
	private List<Double> values = new ArrayList<>();
	private Instant referenceInstant;
	private int missingValues = 0;

	/**
	 * Constructor using a {@linkplain TimeseriesDatasetResponse} and the associated
	 * {@linkplain Instant}.
	 * 
	 * @param timeseriesDatasetResponse
	 * @param referenceInstant
	 */
	public TimeseriesDataset(TimeseriesDatasetResponse timeseriesDatasetResponse, Instant referenceInstant) {

		this.referenceInstant = referenceInstant;

		for (List<String> entry : timeseriesDatasetResponse.getData()) {

			try {
				int timeOffset = Integer.parseInt(entry.get(0));
				double value = Double.parseDouble(entry.get(1));

				timeOffsets.add(timeOffset);
				values.add(value);
			} catch (NumberFormatException e) {
				missingValues += 1;
				// A missing value 'NA' is dropped from the list (but counted)
			}
		}
	}

	/**
	 * The start Instant is calculated using the minimum time offset.
	 * 
	 * @return start {@linkplain Instant} for this timeseries.
	 */
	public Instant getStartInstant() {
		long startEpoch = referenceInstant.getEpochSecond() + Collections.min(timeOffsets);
		return Instant.ofEpochSecond(startEpoch);
	}

	/**
	 * The start Instant is calculated using the maximum time offset.
	 * 
	 * @return stop {@linkplain Instant} for this timeseries.
	 */
	public Instant getStopInstant() {
		long stopEpoch = referenceInstant.getEpochSecond() + Collections.max(timeOffsets);
		return Instant.ofEpochSecond(stopEpoch);
	}

	/**
	 * Calculates simply the mean of of all values
	 * 
	 * @return
	 */
	public double getValuesMean() {

		return values.stream().mapToDouble(a -> a).average().getAsDouble();

	}

	/**
	 * Get number of timeseries values
	 * 
	 * @return number of timeseries values
	 */
	public int getNumberOfValues() {
		return values.size();
	}

	//
	// Getters
	//

	public List<Integer> getTimeOffsets() {
		return timeOffsets;
	}

	public int getMissingValues() {
		return missingValues;
	}

	public List<Double> getValues() {
		return values;
	}

	public Instant getReferenceInstant() {
		return referenceInstant;
	}

}