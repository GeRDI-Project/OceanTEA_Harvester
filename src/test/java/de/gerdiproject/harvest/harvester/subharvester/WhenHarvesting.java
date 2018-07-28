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
//package de.gerdiproject.harvest.bdd.stages_integration;
package de.gerdiproject.harvest.harvester.subharvester;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.google.gson.GsonBuilder;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.OceanTeaDownloader;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.harvest.utils.HashGenerator;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.GsonUtils;

/**
 * This When stage starts the actual harvesting of the given JSON response
 * strings. For this the retrieval via HTTP request from OceanTEA is intercepted
 * using mocking.
 *
 * @author Ingo Thomsen
 */
public class WhenHarvesting extends Stage<WhenHarvesting>
{
    @ExpectedScenarioState
    String allTimeSeriesJSONResponse;

    @ExpectedScenarioState
    String allDataTypesJSONResponse;

    @ExpectedScenarioState
    String timeSeriesDatasetJSONResponse;

    @ProvidedScenarioState
    List<IDocument> resultingIDocuments = new ArrayList<>();


    /**
     * Private generic helper to define for a mocked HttpRequester the behavior of
     * the getObjectFromUrl() method: If requesting a JSON response for a specific
     * targetClass the respective JSON string - provided through the scenario state
     * variable - is returned.
     *
     * @param mock the mocked HttpRequester
     * @param targetClass the requested target class
     * @param aJSONResponse the JSON response string to be returned
     */
    private <T> void setStubReturnValue(HttpRequester mock, Class<T> targetClass, String aJSONResponse)
    {
        T value = GsonUtils.getGson().fromJson(aJSONResponse, targetClass);
        Mockito.when(mock.getObjectFromUrl(Mockito.anyString(), ArgumentMatchers.eq(targetClass))).thenReturn(value);
    }


    /**
     * Create a mock HttpRequester, set up stubbed methods to return the JSOn
     * strings (provided as ScenarioState) and inject it into
     * {@linkplain OceanTeaDownloader}
     */
    @BeforeStage
    @SuppressWarnings("PMD.EmptyCatchBlock") // There is really nothing to do if already initialized
    private void prepareHarvesterLibaryForMockAccess()
    {
        // ensure GsonUtils are initialized
        try {
            GsonUtils.init(new GsonBuilder());
        } catch (IllegalStateException e) {
        }

        // create a mock HttpRequester for intercepting JSON requests
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);

        // set returned objects (created from JSON strings) for the stubbed methods
        setStubReturnValue(mockHttpRequester, AllTimeSeriesResponse.class, allTimeSeriesJSONResponse);
        setStubReturnValue(mockHttpRequester, AllDataTypesResponse.class, allDataTypesJSONResponse);
        setStubReturnValue(mockHttpRequester, TimeSeriesDatasetResponse.class, timeSeriesDatasetJSONResponse);

        // inject mock into the static OceanTeaDownloader
        Whitebox.setInternalState(OceanTeaDownloader.class, HttpRequester.class, mockHttpRequester);
    }


    public WhenHarvesting harvested()
    {
        HashGenerator.init(StandardCharsets.UTF_8);

        TimeSeriesHarvester harvester = new TimeSeriesHarvester();

        Collection<TimeSeries> collectionOfTimeSeries = harvester.loadEntries();

        for (TimeSeries timeSeries : collectionOfTimeSeries) {

            List<IDocument> listOfIDocuments = harvester.harvestEntry(timeSeries);
            resultingIDocuments.addAll(listOfIDocuments);
        }

        return self();
    }
}
