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
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.bdd.stages_integration.GivenTimeSeriesTestData;
import de.gerdiproject.harvest.bdd.stages_integration.ThenResultingDataCiteProperties;
import de.gerdiproject.harvest.bdd.stages_integration.WhenHarvesting;
import de.gerdiproject.harvest.bdd.tags.Issue;
import de.gerdiproject.harvest.bdd.tags.Tag;
import de.gerdiproject.harvest.bdd.tags.TagIntegrationTest;
import de.gerdiproject.json.datacite.enums.ContributorType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.NameType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.extension.constants.ResearchDisciplineConstants;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;

/**
 * The integration tests of the DataCite properties of the created
 * {@linkplain IDocument}: some are dependent of the actual time series dataset
 * and some are independent. The scenarios use data providers for the multiple
 * test cases.
 *
 * @author Ingo Thomsen
 */
@Issue("SAI-312")
@TagIntegrationTest
@Tag("DataCite")
@RunWith(DataProviderRunner.class)
public class PropertiesOfAHarvestedDocument extends ScenarioTest<GivenTimeSeriesTestData, WhenHarvesting, ThenResultingDataCiteProperties>
{

    @DataProvider
    public static Object[][] dataProviderConstants()
    {
        return new Object[][] {

        {
            "resource type", ResourceTypeGeneral.Dataset, "JSON"
        }, {
            "publisher", "OceanTEA demo, Software Engineering, Computer Science, Kiel University"
        },
               };
    }

    @DataProvider
    public static Object[][] dataProviderListConstants()
    {
        return new Object[][] {
            {
                "formats", "application/json",
            }, {
                "subjects", "MoLab"
            }, {
                "subjects", "modular ocean laboratory",
            }, {
                "subjects", "underwater measurement",
            }, {
                "subjects", "oceanography",
            }, {
                "research disciplines", ResearchDisciplineConstants.OCEANOGRAPHY
            }, {
                "weblinks", WebLinkType.Related, "https://oceanrep.geomar.de/22245/"
            }, {
                "weblinks", WebLinkType.ProviderLogoURL, "http://www.se.informatik.uni-kiel.de/en/research/pictures/research-projects/oceantea-logo.png"
            }, {
                "descriptions", DescriptionType.Abstract, "Underwater measurements captured by a MoLab device (modular ocean laboratory) by GEOMAR, Kiel, Germany"
            }, {
                "creators", NameType.Organisational, "GEOMAR, Kiel, Germany"
            }, {
                "contributors", ContributorType.Producer, "GEOMAR, Kiel, Germany"
            },

        };
    }

    @DataProvider
    public static Object[][] dataProviderListVariables()
    {
        return new Object[][] {
            {
                "titles", "Conductivity measurements, underwater (depth 215.0 m) in the region 'Northern Norway'"
            }, {
                "dates", "2012-06-02T11:48:18Z/2012-06-02T20:03:18Z"
            }, {
                "geolocations", 70.2681, 22.4749666666667, -215.0
            }, {
                "research data list", "http://maui.se.informatik.uni-kiel.de:9090/timeseries/scalar/POS434-156/conductivity/215", "Conductivity measurements, collected underwater (depth 215.0 m) in the open water region 'Northern Norway' by MoLab device MLM", "application/json"
            },

        };
    }

    @Test
    @UseDataProvider("dataProviderConstants")
    public void constant_DataCite_properties_with_single_values(String propertyName, Object... values)
    {
        given().a_random_time_series_data_set();

        when().harvested();

        then().the_DataCite_property_$_is_$(propertyName, values);
    }

    @Test
    @UseDataProvider("dataProviderListConstants")
    public void constant_DataCite_properties_with_lists(String listPropertyName, Object... values)
    {
        given().a_random_time_series_data_set();

        when().harvested();

        then().the_DataCite_list_property_$_contains_$(listPropertyName, values);
    }

    @Test
    @UseDataProvider("dataProviderListVariables")
    public void variable_DataCite_properties_depending_on_the_time_series_dataset(
        String listPropertyName,
        Object... values
    )
    {
        given().one_conductivity_time_series_data_set();

        when().harvested();

        then().the_DataCite_property_$_is_$("year", 2012).and().the_DataCite_list_property_$_contains_$(
            listPropertyName,
            values);
    }
}
