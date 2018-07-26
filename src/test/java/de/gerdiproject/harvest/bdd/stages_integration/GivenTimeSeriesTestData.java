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

import com.tngtech.jgiven.CurrentStep;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.attachment.Attachment;
import com.tngtech.jgiven.attachment.MediaType;

import de.gerdiproject.harvest.TestDataProvider;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * Each step (method) in this Given stage (class) selects the JSON responses
 * from the {@linkplain TestDataProvider} that are used to initialize
 * {@linkplain AllTimeSeriesResponse }, {@linkplain AllDataTypesResponse} and
 * {@linkplain TimeSeriesDatasetResponse}.
 *
 * @author Ingo Thomsen
 *
 */
public class GivenTimeSeriesTestData extends Stage<GivenTimeSeriesTestData>
{
    @ProvidedScenarioState
    String allTimeSeriesJSONResponse;

    @ProvidedScenarioState
    String allDataTypesJSONResponse;

    @ProvidedScenarioState
    String timeSeriesDatasetJSONResponse;

    @ProvidedScenarioState
    DataCiteJson expectedDataCiteJson;

    @ScenarioState
    CurrentStep currentStep;


    public GivenTimeSeriesTestData a_time_series_data_set_named_$(String name)
    {
        allDataTypesJSONResponse = TestDataProvider.getAllDataTypesJSON("all");
        timeSeriesDatasetJSONResponse = TestDataProvider.getTimeSeriesDatasetJSON(name);
        allTimeSeriesJSONResponse = TestDataProvider.getAllTimeSeriesJSON(name);

        addJSONStringAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }


    public GivenTimeSeriesTestData an_expected_DataCiteJSON_named_$(String name)
    {
        expectedDataCiteJson = TestDataProvider.getExpectedtDataCiteJSON(name);

        addJSONStringAsAttachmentAndDescription(GsonUtils.getPrettyGson().toJson(expectedDataCiteJson));

        return self();
    }


    public GivenTimeSeriesTestData a_random_time_series_data_set()
    {
        allDataTypesJSONResponse = TestDataProvider.getAllDataTypesJSON("all");
        allTimeSeriesJSONResponse = TestDataProvider.getRandomAllTimeSeriesJSON();
        timeSeriesDatasetJSONResponse = TestDataProvider.getTimeSeriesDatasetJSON("synthetic.no_data");

        addJSONStringAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }


    /**
     * A private helper to add the JSON string as attachment for the HTML report on
     * this stage and set the description (e. g. shown as tooltip) accordingly.
     *
     * @param aJSONResponse The JSON string that is to be added to the HTML report
     */
    private void addJSONStringAsAttachmentAndDescription(String aJSONResponse)
    {
        Attachment attachment = Attachment.fromText(aJSONResponse, MediaType.JSON_UTF_8);
        currentStep.addAttachment(attachment);

        currentStep.setExtendedDescription(
            "The applied JSON responses are attached - numbered accordingly in the event of multiple calls to this step due to multiple cases.");
    }
}