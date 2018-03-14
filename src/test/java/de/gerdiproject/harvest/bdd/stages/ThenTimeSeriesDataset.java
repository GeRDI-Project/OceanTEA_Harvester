package de.gerdiproject.harvest.bdd.stages;

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

    // @ExpectedScenarioState
    // int returnValue;
    //
    // @ExpectedScenarioState
    // ArithmeticException caughtArithmeticException;
    //
    // public ThenResults the_result_is_6() {
    //
    // Assert.assertEquals(6, returnValue);
    // return self();
    // }
    //
    //
    // public ThenResults the_result_is_$(int c) {
    //
    // Assert.assertEquals(c, returnValue);
    // return self();
    // }
    //
    //
    // public ThenResults an_ArithmeticException_is_thrown() {
    //
    // Assert.assertNotNull(caughtArithmeticException);
    // return self();
    // }
}
