/**
 * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de) Licensed under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.gerdiproject.harvest.bdd.scenarios;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.bdd.stages.given.GivenTimeSeriesDatasetResponse;
import de.gerdiproject.harvest.bdd.stages.then.ThenTimeSeriesDataset;
import de.gerdiproject.harvest.bdd.stages.when.WhenTimeSeriesDataset;
import de.gerdiproject.harvest.bdd.tags.Tag;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesDataset;

/**
 * This scenario describes some initialization behavior of the
 * {@linkplain TimeSeriesDataset} class.
 *
 * @author Ingo Thomsen
 */
@Tag("Initialization")
@RunWith(DataProviderRunner.class)
public class InitializationOfATimeSeriesDataset extends ScenarioTest<GivenTimeSeriesDatasetResponse, WhenTimeSeriesDataset, ThenTimeSeriesDataset>
{

    @Test
    public void minimal_number_of_entries()
    {
        given().a_TimeSeriesDatasetResponse_with_boundary_time_offsets_$_and_$(-100, 100);
        when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse("2013-05-30T23:38:23Z"));
        then().the_TimeSeriesDataset_has_at_least_$_values(2);
    }


    @Test
    public void missing_time_series_data()
    {
        given().an_empty_TimeSeriesDatasetResponse();
        when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse("2013-05-30T23:38:23Z"));
        then().the_TimeSeriesDataset_has_no_values().and().startInstant_and_stopInstant_are_equal();
    }


    @Test
    public void only_one_time_series_entry()
    {
        given().an_TimeSeriesDatasetResponse_with_one_random_value_pair();
        when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse("2013-05-30T23:38:23Z"));
        then().startInstant_and_stopInstant_are_equal();
    }


    /**
     * The {@linkplain Instant} are given as strings in ISO-8601 format
     */
    @DataProvider({
        // @formatter:off
        "2013-05-30T23:38:23Z,  0,  0, 2013-05-30T23:38:23Z, 2013-05-30T23:38:23Z",
        "2013-05-30T23:59:30Z, 30,-30, 2013-05-30T23:59:00Z, 2013-05-31T00:00:00Z",
        "2013-05-30T23:38:23Z, 30,-30, 2013-05-30T23:37:53Z, 2013-05-30T23:38:53Z",
        // "2014-05-30T23:38:23Z, 30,-30, 2013-05-30T23:37:53Z, 2013-05-30T23:38:53Z", // this would be a failing case for example
        // @formatter:on
    })
    @Test
    public void calculation_of_start_and_stop_instant(
        String referenceInstant,
        int offset1,
        int offset2,
        String startInstant,
        String stopInstant
    )
    {
        given().a_TimeSeriesDatasetResponse_with_boundary_time_offsets_$_and_$(offset1, offset2);

        when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse(referenceInstant));

        then().the_TimeSeriesDataset_startInstant_is_$(Instant.parse(startInstant)).and()
        .the_TimeSeriesDataset_stopInstant_is_$(Instant.parse(stopInstant));
    }
}