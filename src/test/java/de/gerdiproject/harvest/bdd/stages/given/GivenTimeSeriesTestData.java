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
package de.gerdiproject.harvest.bdd.stages.given;

import com.google.gson.Gson;
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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A stage using the {@linkplain TestDataProvider} with steps to provide certain
 * instances of {@linkplain AllTimeSeriesResponse},
 * {@linkplain AllDataTypesResponse} and {@linkplain TimeSeriesDatasetResponse}
 * to the When stage.
 *
 * @author Ingo Thomsen
 */
public class GivenTimeSeriesTestData extends Stage<GivenTimeSeriesTestData>
{
    private static final Gson GSON = GsonUtils.createGerdiDocumentGsonBuilder().setPrettyPrinting().create();

    @ProvidedScenarioState
    String allTimeSeriesJSONResponse;

    @ProvidedScenarioState
    @SuppressFBWarnings("URF_UNREAD_FIELD") // As ProvidedScenarioState this is read by another Stage class
    String allDataTypesJSONResponse;

    @ProvidedScenarioState
    @SuppressFBWarnings("URF_UNREAD_FIELD") // As ProvidedScenarioState this is read by another Stage class
    String timeSeriesDatasetJSONResponse;

    @ProvidedScenarioState
    DataCiteJson expectedDataCiteJson;

    @ScenarioState
    CurrentStep currentStep;


    public GivenTimeSeriesTestData a_time_series_dataset_named_$(String name)
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

        addJSONStringAsAttachmentAndDescription(GSON.toJson(expectedDataCiteJson));

        return self();
    }


    public GivenTimeSeriesTestData a_random_time_series_dataset()
    {
        allDataTypesJSONResponse = TestDataProvider.getAllDataTypesJSON("all");
        allTimeSeriesJSONResponse = TestDataProvider.getRandomAllTimeSeriesJSON();
        timeSeriesDatasetJSONResponse = TestDataProvider.getTimeSeriesDatasetJSON("synthetic.no_data");

        addJSONStringAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }


    public GivenTimeSeriesTestData all_time_series_datasets()
    {
        allDataTypesJSONResponse = TestDataProvider.getAllDataTypesJSON("all");
        allTimeSeriesJSONResponse = TestDataProvider.getAllTimeSeriesJSON();
        timeSeriesDatasetJSONResponse = TestDataProvider.getTimeSeriesDatasetJSON("synthetic.no_data");

        addJSONStringAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }


    /**
     * A private helper to add the JSON string as attachment for the HTML report on
     * this stage and set the description (e. g. shown as tool tip) accordingly.
     *
     * @param aJSONResponse The JSON string that is to be added to the HTML report
     */
    private void addJSONStringAsAttachmentAndDescription(String aJSONResponse)
    {
        Attachment attachment = Attachment.fromText(aJSONResponse, MediaType.JSON_UTF_8);
        currentStep.addAttachment(attachment);

        currentStep.setExtendedDescription("The given resp. expected JSON strings are attached.");
    }

}
