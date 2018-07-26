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
package de.gerdiproject.harvest.bdd.scenarios;

import org.junit.Test;

import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.bdd.stages.given.GivenTimeSeriesTestData;
import de.gerdiproject.harvest.bdd.stages.then.ThenResultingIDocuments;
import de.gerdiproject.harvest.bdd.tags.Issue;
import de.gerdiproject.harvest.bdd.tags.Tag;
import de.gerdiproject.harvest.harvester.subharvester.WhenHarvesting;

/**
 * Scenario about the general structure of list of the resulting IDocuments.
 *
 * @author Ingo Thomsen
 */
@Issue("SAI-312")
@Tag("DataCite")
public class HarvestedDocumentForSingleTimeSeriesDataset extends ScenarioTest<GivenTimeSeriesTestData, WhenHarvesting, ThenResultingIDocuments>
{

    @Test
    public void one_conductivity_TimeSeries_dataset()
    {
        given().a_random_time_series_data_set();

        when().harvested();

        then().there_is_exactly_one_resulting_document();
    }

}