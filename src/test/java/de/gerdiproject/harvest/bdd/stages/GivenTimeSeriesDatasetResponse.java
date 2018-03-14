package de.gerdiproject.harvest.bdd.stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;

public class GivenTimeSeriesDatasetResponse extends Stage<GivenTimeSeriesDatasetResponse>
{

    @ProvidedScenarioState
    TimeSeriesDatasetResponse timeSeriesDatasetResponse;

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

}
