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
package de.gerdiproject.harvest.bdd.stages.oceantea.utils;

import java.time.Instant;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesDataset;

/**
 * A When stage with steps methods for the {@linkplain TimeSeriesDataset} class.
 *
 * @author Ingo Thomsen
 */
public class WhenTimeSeriesDataset extends Stage<WhenTimeSeriesDataset>
{

    @ExpectedScenarioState
    TimeSeriesDatasetResponse timeSeriesDatasetResponse;

    @ProvidedScenarioState
    TimeSeriesDataset timeSeriesDataset;

    public WhenTimeSeriesDataset a_TimeSeriesDataset_is_created_for_Instant(Instant referenceInstant)
    {
        timeSeriesDataset = new TimeSeriesDataset(timeSeriesDatasetResponse, referenceInstant);
        return self();
    }
}
