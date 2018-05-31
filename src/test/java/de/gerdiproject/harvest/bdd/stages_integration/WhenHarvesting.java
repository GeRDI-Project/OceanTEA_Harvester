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
package de.gerdiproject.harvest.bdd.stages_integration;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.google.gson.GsonBuilder;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.harvester.subharvester.TimeSeriesHarvester;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.harvest.oceantea.utils.OceanTeaDownloader;
import de.gerdiproject.harvest.utils.cache.HarvesterCache;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.GsonUtils;

public class WhenHarvesting extends Stage<WhenHarvesting>
{
    @ExpectedScenarioState
    String allTimeSeriesJsonResponseString;

    @ExpectedScenarioState
    String allDataTypesJsonResponseString;

    @ExpectedScenarioState
    String timeSeriesDatasetJsonResponseString;

    @ProvidedScenarioState
    List<IDocument> resultingIDocuments = new ArrayList<>();

    private <T> void setStubReturnValue(HttpRequester mock, Class<T> targetClass, String jsonResponseString)
    {
        T value = GsonUtils.getGson().fromJson(jsonResponseString, targetClass);
        Mockito.when(mock.getObjectFromUrl(Mockito.anyString(), ArgumentMatchers.eq(targetClass))).thenReturn(value);
    }

    @BeforeScenario
    public void initializeHarvesterLibrary()
    {
    }

    @BeforeStage
    @SuppressWarnings("PMD.EmptyCatchBlock") // There is really nothing to do if already initialized
    public void prepareHarvesterLibaryForMockAccess()
    {
        // ensure GsonUtils are initialized
        try {
            GsonUtils.init(new GsonBuilder());
        } catch (IllegalStateException e) {
        }

        // create a mock HttpRequester for intercepting JSON requests
        HttpRequester mockHttpRequester = Mockito.mock(HttpRequester.class);

        // set returned objects (created from JSON strings) for the stubbed methods
        setStubReturnValue(mockHttpRequester, AllTimeSeriesResponse.class, allTimeSeriesJsonResponseString);
        setStubReturnValue(mockHttpRequester, AllDataTypesResponse.class, allDataTypesJsonResponseString);
        setStubReturnValue(mockHttpRequester, TimeSeriesDatasetResponse.class, timeSeriesDatasetJsonResponseString);

        // inject mock into the static OceanTeaDownloader
        Whitebox.setInternalState(OceanTeaDownloader.class, HttpRequester.class, mockHttpRequester);
    }

    /*
     * Mocking by using a (nested) subclass the harvester class. It would be
     * possible to mock the original class (by using stubs with call backs and
     * spying on protected methods), but this way it is more transparent.
     */
    class TimeSeriesHarvesterForTesting extends TimeSeriesHarvester
    {
        List<IDocument> harvestedDocuments = new ArrayList<>();

        @Override
        @SuppressWarnings("PMD.EmptyCatchBlock") // With the mock data the is always one document
        public void init()
        {
            super.init();

            try {
                harvestInternal(0, getMaxNumberOfDocuments());
            } catch (Exception e) {}
        }

        @Override
        protected HarvesterCache initCache()
        {
            return new HarvesterCache(name);
        }

        @Override
        protected void addDocument(IDocument document)
        {
            resultingIDocuments.add(document);
        }
    }

    public WhenHarvesting harvested()
    {
        TimeSeriesHarvesterForTesting harvester = new TimeSeriesHarvesterForTesting();

        harvester.init();

        return self();
    }
}