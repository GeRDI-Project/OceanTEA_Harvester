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
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.bdd.stages_integration.GivenTimeSeriesTestData;
import de.gerdiproject.harvest.bdd.stages_integration.ThenResultingDataCiteProperties;
import de.gerdiproject.harvest.bdd.stages_integration.WhenHarvesting;
import de.gerdiproject.harvest.bdd.tags.Issue;
import de.gerdiproject.harvest.bdd.tags.Tag;
import de.gerdiproject.harvest.bdd.tags.TagIntegrationTest;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;

@Issue("SAI-312")
@TagIntegrationTest
@Tag("DataCite")
@RunWith(DataProviderRunner.class)
public class PropertiesOfAHarvestedDocument extends ScenarioTest<GivenTimeSeriesTestData, WhenHarvesting, ThenResultingDataCiteProperties>
{
    @Test
    // formatter:off
    @DataProvider(value = {
        "repositoryIdentifier | OCEANTEA",
        "publisher            | OceanTEA demo, Software Engineering Informatik, Kiel University",
    }, splitBy = "\\|", trimValues = true)
    // formatter:on
    public void genericDataCiteProperties(String propertyName, String stringValue)
    {
        // formatter:off
        given().a_random_time_series_data_set();
        when().harvest();
        then().resourceType_is_$_of_type_$("JSON", ResourceTypeGeneral.Dataset).

        and ().webLinks_contains(WebLinkType.ProviderLogoURL,
                                 "http://www.se.informatik.uni-kiel.de/en/research/pictures/research-projects/oceantea-logo.png")
        .

        and ().webLinks_contains(WebLinkType.Related, "https://oceanrep.geomar.de/22245/").

        and ().string_$_is_$(propertyName, stringValue);
        // formatter:on
    }

    @Test
    public void individualDataCiteProperties()
    {
        // formatter:off
        given().one_conductivity_time_series_data_set();
        when().harvest();
        then().one_title_is("Conductivity measurements, underwater (depth 215.0 m) in the region 'Northern Norway'")
        .and().one_geolocation_is_$_$_$(70.2681, 22.4749666666667, -215.0);
        // formatter:on

    }
}
