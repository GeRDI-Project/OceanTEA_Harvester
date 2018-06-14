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

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // The representation is not the best anyway
class mockJsonResponses
{
    // @formatter:off
    static String mockAllTimeSeriesJsonResponseString     = "{\n" +
                                                            "   \"timeseries\" : [\n" +
                                                            "      {\n" +
                                                            "         \"tsType\" : \"scalar\",\n" +
                                                            "         \"t_reference\" : \"2012-06-01T00:00:01Z\",\n" +
                                                            "         \"lon\" : 22.4749666666667,\n" +
                                                            "         \"dataType\" : \"conductivity\",\n" +
                                                            "         \"lat\" : 70.2681,\n" +
                                                            "         \"regionPrintName\" : \"Northern Norway\",\n" +
                                                            "         \"station\" : \"POS434-156\",\n" +
                                                            "         \"device\" : \"MLM\",\n" +
                                                            "         \"region\" : \"northern-norway\",\n" +
                                                            "         \"depth\" : 215\n" +
                                                            "      }\n" +
                                                            "   ]\n" +
                                                            "}";

    static String mockAllDataTypesJsonResponseString      = "{\n" +
                                                            "   \"conductivity\" : {\n" +
                                                            "      \"printName\" : \"Conductivity\",\n" +
                                                            "      \"unit\" : \"mS/cm\"\n" +
                                                            "   },\n" +
                                                            "   \"potentialDensityAnomaly\" : {\n" +
                                                            "      \"unit\" : \"\",\n" +
                                                            "      \"printName\" : \"Potential Density Anomaly\"\n" +
                                                            "   }\n" +
                                                            "}";

    static String mockTimeSeriesDatasetJsonResponseString = "{\n" +
                                                            "   \"data\" : [\n" +
                                                            "      [\n" +
                                                            "         128897,\n" +
                                                            "         -0.75\n" +
                                                            "      ],\n" +
                                                            "      [\n" +
                                                            "         129197,\n" +
                                                            "         -0.7469\n" +
                                                            "      ],\n" +
                                                            "      [\n" +
                                                            "         129497,\n" +
                                                            "         -0.75\n" +
                                                            "      ],\n" +
                                                            "      [\n" +
                                                            "         129797,\n" +
                                                            "         -0.75\n" +
                                                            "      ],\n" +
                                                            "      [\n" +
                                                            "         130097,\n" +
                                                            "         -0.75\n" +
                                                            "      ],\n" +
                                                            "      [\n" +
                                                            "         130397,\n" +
                                                            "         -0.7492\n" +
                                                            "      ]\n" +
                                                            "   ]\n" +
                                                            "}";

    // @formatter:on
}


public class GivenTimeSeriesTestData extends Stage<GivenTimeSeriesTestData>
{
    @ProvidedScenarioState
    String allTimeSeriesJSONResponse;

    @ProvidedScenarioState
    String allDataTypesJSONResponse;

    @ProvidedScenarioState
    String timeSeriesDatasetJSONResponse;

    @ScenarioState
    CurrentStep currentStep;

    public GivenTimeSeriesTestData one_conductivity_time_series_data_set()
    /**
     * Add the JSON string as attachment for the HTML report on this stage and set
     * the description (e. g. shown as tooltip) accordingly.
     *
     * @param aJSONResponse The JSON string that is to be added to the HTML report
     */
    private void addJSONStrinAsAttachmentAndDescription(String aJSONResponse)
    {
        allTimeSeriesJsonResponseString = mockJsonResponses.mockAllTimeSeriesJsonResponseString;
        allDataTypesJsonResponseString = mockJsonResponses.mockAllDataTypesJsonResponseString;
        timeSeriesDatasetJsonResponseString = mockJsonResponses.mockTimeSeriesDatasetJsonResponseString;

        Attachment attachment = Attachment.fromText(aJSONResponse, MediaType.JSON_UTF_8);
        currentStep.addAttachment(attachment);

        currentStep.setExtendedDescription(
            "The applied JSON responses are attached - numbered accordingly in the event of multiple calls to this step due to multiple cases.");
    }


        addJSONStrinAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }

    public GivenTimeSeriesTestData a_random_time_series_data_set()
    {
        allTimeSeriesJsonResponseString = mockJsonResponses.mockAllTimeSeriesJsonResponseString;
        allDataTypesJsonResponseString = mockJsonResponses.mockAllDataTypesJsonResponseString;
        timeSeriesDatasetJsonResponseString = mockJsonResponses.mockTimeSeriesDatasetJsonResponseString;

        addJSONStrinAsAttachmentAndDescription(allTimeSeriesJSONResponse);

        return self();
    }
}
