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
package de.gerdiproject.harvest.bdd.scenarios_integration;

import org.junit.Test;

import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.bdd.stages_integration.GivenTimeSeriesTestData;
import de.gerdiproject.harvest.bdd.stages_integration.ThenResultingIDocuments;
import de.gerdiproject.harvest.bdd.tags.Issue;
import de.gerdiproject.harvest.bdd.tags.TagIntegrationTest;
import de.gerdiproject.harvest.harvester.subharvester.WhenHarvesting;

/**
 * A scenario with an exemplary integration test
 *
 * @author Ingo Thomsen
 */
@Issue("SAI-312")
@TagIntegrationTest
public class HarvestedDocumentForSingleTimeSeriesDataSet extends ScenarioTest<GivenTimeSeriesTestData, WhenHarvesting, ThenResultingIDocuments>
{
    @Test
    public void one_conductivity_TimeSeries_dataset()
    {
        given().one_conductivity_time_series_data_set();
        when().harvested();
        then().one_resulting_document();
    }
}