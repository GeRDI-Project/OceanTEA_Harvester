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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.oceantea.utils.TimeSeriesDataset;

public class ThenTimeSeriesDataset extends Stage<ThenTimeSeriesDataset>
{

    @ExpectedScenarioState
    TimeSeriesDataset timeSeriesDataset;


    public ThenTimeSeriesDataset the_TimeSeriesDataset_startInstant_is_$(Instant instant)
    {
        assertThat(instant).isEqualTo(timeSeriesDataset.getStartInstant());
        return self();
    }

    public ThenTimeSeriesDataset the_TimeSeriesDataset_stopInstant_is_$(Instant instant)
    {
        assertThat(instant).isEqualTo(timeSeriesDataset.getStopInstant());
        return self();
    }

    public ThenTimeSeriesDataset the_TimeSeriesDataset_has_at_least_$_values(int count)
    {
        assertThat(timeSeriesDataset.getNumberOfValues()).isGreaterThanOrEqualTo(count);
        return self();
    }

    public ThenTimeSeriesDataset the_TimeSeriesDataset_has_no_values()
    {
        assertThat(timeSeriesDataset.getNumberOfValues()).isZero();
        return self();
    }


    public ThenTimeSeriesDataset startInstant_and_stopInstant_are_equal()
    {
        assertThat(timeSeriesDataset.getStartInstant()).isEqualTo(timeSeriesDataset.getStopInstant());
        return self();
    }

}