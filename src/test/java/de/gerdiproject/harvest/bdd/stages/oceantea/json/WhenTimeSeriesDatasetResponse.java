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
package de.gerdiproject.harvest.bdd.stages.oceantea.json;

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
