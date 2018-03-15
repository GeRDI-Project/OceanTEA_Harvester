package de.gerdiproject.harvest.bdd.scenarios;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.bdd.stages.GivenTimeSeriesDatasetResponse;
import de.gerdiproject.harvest.bdd.stages.ThenTimeSeriesDataset;
import de.gerdiproject.harvest.bdd.stages.WhenTimeSeriesDataset;

// import org.junit.jupiter.api.Test;
// import com.tngtech.jgiven.junit5.ScenarioTest;

@RunWith(DataProviderRunner.class)
public class Initializing_a_TimeSeriesDataset extends ScenarioTest<GivenTimeSeriesDatasetResponse, WhenTimeSeriesDataset, ThenTimeSeriesDataset>
{

    // @Test
    // public void calculation_of_start_and_stop_instant()
    // {
    //
    // // The {@linkplain Instant} are given as strings in ISO-8601 format
    // int min_time_offset = -63;
    // int max_time_offset = 70;
    // String referenceInstant = "2013-05-30T23:38:23Z";
    // String startInstant = "2013-05-30T23:37:20Z";
    // String stopInstant = "2013-05-30T23:39:33Z";
    //
    // given().a_TimeSeriesDatasetResponse_with_min_and_max_time_offset_of_$_and_$(min_time_offset,
    // max_time_offset);
    //
    // when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse(referenceInstant));
    //
    // then().the_TimeSeriesDataset_startInstant_is_$(Instant.parse(startInstant)).and()
    // .the_TimeSeriesDataset_stopInstant_is_$(Instant.parse(stopInstant));
    // }

    /**
     * The {@linkplain Instant} are given as strings in ISO-8601 format
     */
    @DataProvider({
        // @formatter:off
        "2013-05-30T23:38:23Z, 0,0, 2013-05-30T23:38:23Z, 2013-05-30T23:38:23Z",
        "2014-05-30T23:37:23Z, 30,-30, 2014-05-30T23:37:53Z, 2014-05-30T23:38:53Z",
        "2013-05-30T23:38:23Z, 30,-30, 2013-05-30T23:37:53Z, 2013-05-30T23:38:53Z"

        // @formatter:on
    })
    @Test
    public void start_and_stop_instant(String referenceInstant, int offset1, int offset2, String start, String stop)
    {

        int i = 1;
        given().a_TimeSeriesDatasetResponse_with_boundary_time_offsets_$_and_$(offset1, offset2);

        when().a_TimeSeriesDataset_is_created_for_Instant(Instant.parse(referenceInstant));

        then().the_TimeSeriesDataset_startInstant_is_$(Instant.parse(start)).and()
            .the_TimeSeriesDataset_stopInstant_is_$(Instant.parse(stop));
    }
}