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

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.bdd.stages.given.GivenTimeSeriesTestData;
import de.gerdiproject.harvest.bdd.stages.then.ThenResultingDataCiteProperties;
import de.gerdiproject.harvest.bdd.tags.Tag;

/**
 * Scenarios on how given OceanTEA data is translated into valid
 * {@linkplain IDocument}s. Some of the resulting properties are independent of
 * the specific dataset while others are partly or even fully dependent.
 *
 * @author Ingo Thomsen
 */
@Tag("DataCite")
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // The assertions are done in the stages
public class PropertiesOfAHarvestedDocument extends AbstractHarvestingScenarioTest<GivenTimeSeriesTestData, ThenResultingDataCiteProperties>
{

    @Test
    public void constant_DataCite_properties_for_random_time_series_dataset()
    {
        // @formatter:off

        given().a_random_time_series_dataset().
        and ().an_expected_DataCiteJSON_named_$("constant_properties");

        when().harvested();

        then().all_constant_DataCite_properties_equal_those_in_expected_DataCiteJSON().
        and ().all_partly_variable_DataCite_properties_contain_those_in_expected_DataCiteJSON();

        // @formatter:on
    }


    @Test
    public void variable_DataCite_properties_for_exemplary_time_series_dataset()
    {
        // @formatter:off

        given().a_time_series_dataset_named_$("POS434-156_conductivity_215").
        and ().an_expected_DataCiteJSON_named_$("POS434-156_conductivity_215");

        when().harvested();

        then().all_partly_variable_DataCite_properties_contain_those_in_expected_DataCiteJSON().
        and ().all_variable_DataCite_properties_equal_those_in_expected_DataCiteJSON();

        // @formatter:on
    }
}