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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;

/**
 * A Given stage with steps for the {@linkplain TimeSeriesDatasetResponse} class.
 *
 * @author Ingo Thomsen
 */
public class GivenTimeSeriesDatasetResponse extends Stage<GivenTimeSeriesDatasetResponse>
{

    @ProvidedScenarioState
    TimeSeriesDatasetResponse timeSeriesDatasetResponse;

    public GivenTimeSeriesDatasetResponse an_empty_TimeSeriesDatasetResponse()
    {
        timeSeriesDatasetResponse = new TimeSeriesDatasetResponse();
        return self();
    }


    public GivenTimeSeriesDatasetResponse a_TimeSeriesDatasetResponse_with_boundary_time_offsets_$_and_$(int v1, int v2)
    {
        // create list of lists
        Random random = new Random();
        List<List<String>> data = new ArrayList<>();

        data.add(Arrays.asList(Integer.toString(v1), Integer.toString(random.nextInt())));
        data.add(Arrays.asList(Integer.toString(v2), Integer.toString(random.nextInt())));

        timeSeriesDatasetResponse = new TimeSeriesDatasetResponse();
        timeSeriesDatasetResponse.setListOfPairsOfTimeOffsetAndValue(data);
        return self();
    }


    public GivenTimeSeriesDatasetResponse an_TimeSeriesDatasetResponse_with_one_random_value_pair()
    {
        Random random = new Random();
        List<List<String>> data = new ArrayList<>();

        data.add(Arrays.asList(Integer.toString(random.nextInt()), Integer.toString(random.nextInt())));

        timeSeriesDatasetResponse = new TimeSeriesDatasetResponse();
        timeSeriesDatasetResponse.setListOfPairsOfTimeOffsetAndValue(data);
        return self();
    }
}