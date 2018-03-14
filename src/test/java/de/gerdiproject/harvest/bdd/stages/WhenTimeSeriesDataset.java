package de.gerdiproject.harvest.bdd.stages;

import java.time.Instant;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.TimeSeriesDataset;

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

    // @ProvidedScenarioState
    // int returnValue;
    //
    // @ProvidedScenarioState
    // ArithmeticException caughtArithmeticException;
    //
    // public void I_add_4_and_2()
    // {
    //
    // // returnValue = simpleMathObject.add(4, 2);
    //
    // }
    //
    // public void I_add_$_and_$(int a, int b)
    // {
    //
    // // returnValue = simpleMathObject.add(a, b);
    //
    // }
    //
    // public void I_divide_1_by_0()
    // {
    //
    // try {
    // // returnValue = simpleMathObject.divide(1, 0);
    // } catch (ArithmeticException e) {
    //
    // caughtArithmeticException = e;
    // }
    // }

}
