package de.gerdiproject.harvest.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.oceantea.utils.TimeSeriesDataset;

public class WhenTimeSeriesDatasetResponse extends Stage<WhenTimeSeriesDatasetResponse>
{

    @ExpectedScenarioState
    TimeSeriesDataset timeSeriesDataset;

//    @ProvidedScenarioState
//    int returnValue;
//
//    @ProvidedScenarioState
//    ArithmeticException caughtArithmeticException;
//
//    public void I_add_4_and_2()
//    {
//
//        // returnValue = simpleMathObject.add(4, 2);
//
//    }
//
//    public void I_add_$_and_$(int a, int b)
//    {
//
//        // returnValue = simpleMathObject.add(a, b);
//
//    }
//
//    public void I_divide_1_by_0()
//    {
//
//        try {
//            // returnValue = simpleMathObject.divide(1, 0);
//        } catch (ArithmeticException e) {
//
//            caughtArithmeticException = e;
//        }
//    }

}
