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
package de.gerdiproject.harvest.etl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.google.gson.Gson;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.etl.TimeSeriesETL;
import de.gerdiproject.harvest.etl.extractors.TimeSeriesExtractor;
import de.gerdiproject.harvest.etl.transformers.TimeSeriesTransformer;
import de.gerdiproject.harvest.etls.loaders.AbstractIteratorLoader;
import de.gerdiproject.harvest.etls.loaders.LoaderException;
import de.gerdiproject.harvest.etls.loaders.events.CreateLoaderEvent;
import de.gerdiproject.harvest.event.EventSystem;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * This When stage starts the actual harvesting of the given JSON response
 * strings. For this the retrieval via HTTP request from OceanTEA is intercepted
 * using mocking.
 *
 * @author Ingo Thomsen
 */
public class WhenHarvesting extends Stage<WhenHarvesting>
{
    private static Gson GSON = new Gson();

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
        T value = GSON.fromJson(aJSONResponse, targetClass);
        Mockito.when(mock.getObjectFromUrl(Mockito.anyString(), ArgumentMatchers.eq(targetClass))).thenReturn(value);
    }


    /**
     * Create a mock HttpRequester, set up stubbed methods to return the JSON
     * strings (provided as ScenarioState).
     */
    @BeforeStage
    public void prepareHarvesterLibaryForMockAccess()
    {
        // create a mock HttpRequester for intercepting JSON requests
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);

        // set returned objects (created from JSON strings) for the stubbed methods
        setStubReturnValue(mockHttpRequester, AllTimeSeriesResponse.class, allTimeSeriesJSONResponse);
        setStubReturnValue(mockHttpRequester, AllDataTypesResponse.class, allDataTypesJSONResponse);
        setStubReturnValue(mockHttpRequester, TimeSeriesDatasetResponse.class, timeSeriesDatasetJSONResponse);

        // inject mock into the static OceanTeaDownloader
        Whitebox.setInternalState(TimeSeriesExtractor.class, HttpRequester.class, mockHttpRequester);
        Whitebox.setInternalState(TimeSeriesTransformer.class, HttpRequester.class, mockHttpRequester);
    }


    public WhenHarvesting harvested()
    {
        // make sure harvested documents are loaded into 'resultingIDocuments'
        EventSystem.addSynchronousListener(CreateLoaderEvent.class, (CreateLoaderEvent e) -> new MockedLoader());
        final TimeSeriesETL etl = new TimeSeriesETL();
        EventSystem.removeSynchronousListener(CreateLoaderEvent.class);

        etl.prepareHarvest();
        etl.harvest();

        return self();
    }


    /**
     * This test Loader stores harvested documents in 'resultingIDocuments'.
     *
     * @author Robin Weiss
     */
    private class MockedLoader extends AbstractIteratorLoader<DataCiteJson>
    {
        @Override
        public void unregisterParameters()
        {
            // nothing to do here
        }


        @Override
        public void clear()
        {
            // nothing to do here
        }


        @Override
        protected void loadElement(DataCiteJson document) throws LoaderException
        {
            resultingIDocuments.add(document);
        }
    }
}
