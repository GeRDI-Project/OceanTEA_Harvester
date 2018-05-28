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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;
import de.gerdiproject.json.geo.Point;

public class ThenResultingDataCiteProperties extends Stage<ThenResultingDataCiteProperties>
{
    @ExpectedScenarioState
    List<IDocument> resultingIDocuments = new ArrayList<>();

    DataCiteJson dataCiteJson;

    @BeforeStage
    @SuppressWarnings("PMD.UnusedPrivateMethod") // This method IS called via @BeforeStage
    private void extractDataCiteJsonFromFirstEntry()
    {
        dataCiteJson = (DataCiteJson) resultingIDocuments.get(0);
    }

    public ThenResultingDataCiteProperties resourceType_is_$_of_type_$(String value, ResourceTypeGeneral dataset)
    {
        ResourceType resourceType = dataCiteJson.getResourceType();

        assertThat(resourceType.getValue()).isEqualTo(value);
        assertThat(resourceType.getGeneralType()).isEqualTo(dataset);

        return self();
    }

    @As("'$':")
    public ThenResultingDataCiteProperties string_$_is_$(String propertyName, String value)
    {
        //
        // Get the value from the dataCiteJson, assuming a Bean-like getter like
        // getProperty()
        //
        String actualValue = "";

        String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        try {
            Method getterMethod = DataCiteJson.class.getDeclaredMethod(getterMethodName);
            actualValue = (String) getterMethod.invoke(dataCiteJson);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new AssertionError(
                "Cannot access property '" + propertyName + "' of dataCiteJson using method *" + getterMethodName + "()'");
        }

        assertThat(actualValue).isEqualTo(value);

        return self();
    }

    @As("one $ webLink is: $")
    public ThenResultingDataCiteProperties webLinks_contains(WebLinkType webLinkType, String url)
    {
        boolean webLinkFound = false;

        for (WebLink webLink : dataCiteJson.getWebLinks())
            if (webLink.getUrl() == url & webLink.getType() == webLinkType)
                webLinkFound = true;

        assertThat(webLinkFound).isTrue();

        return self();
    }

    @As("one title is: $")
    public ThenResultingDataCiteProperties one_title_is(String value)
    {
        assertThat(value).isIn(dataCiteJson.getTitles().stream().map(x -> x.getValue()).collect(Collectors.toList()));

        return self();
    }

    @As("one geolocation is $ latidute,$ longitute and$ elevation")
    public ThenResultingDataCiteProperties one_geolocation_is_$_$_$(double latitude, double longitude, double elevation)
    {
        for (GeoLocation geoLocation : dataCiteJson.getGeoLocations()) {

            Point point = (Point) geoLocation.getPoint().getCoordinates();

            assertThat(point.getLatitude()).isEqualTo(latitude);
            assertThat(point.getLongitude()).isEqualTo(longitude);
            assertThat(point.getElevation()).isEqualTo(elevation);

//            System.out.println("-------------");
//            System.out.println(point.getLatitude());
//            System.out.println(point.getLongitude());
//            System.out.println(point.getElevation());

        }

        return self();
    }
}