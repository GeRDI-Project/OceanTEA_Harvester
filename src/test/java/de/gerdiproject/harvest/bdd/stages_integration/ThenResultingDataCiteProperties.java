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
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.json.datacite.Contributor;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.extension.ResearchData;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.geo.Point;

/**
 * This Then stages checks the DataCite properties of the first created
 * IDocument.
 *
 * There are two step methods to check the properties, depending on whether they
 * contain lists or not.
 *
 * @author Ingo Thomsen
 */
public class ThenResultingDataCiteProperties extends Stage<ThenResultingDataCiteProperties>
{
    @ExpectedScenarioState
    List<IDocument> resultingIDocuments = new ArrayList<>();

    DataCiteJson dataCiteJSON;

    /**
     * Private method to make the first document of the list resulting IDocuments
     * available to the step methods in this state class. This is called before any
     * stage methods.
     */
    @BeforeStage
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void extractDataCiteJSONFromFirstEntry()
    {
        dataCiteJSON = (DataCiteJson) resultingIDocuments.get(0);
    }

    // step method
    public ThenResultingDataCiteProperties the_DataCite_property_$_is_$(String propertyName, Object... values)
    {

        switch (propertyName) {

            case "resource type":
                // excepts a ResourceTypeGeneral and a string description

                ResourceType resourceType = dataCiteJSON.getResourceType();

                assertThat(resourceType.getGeneralType()).isEqualTo(values[0]);
                assertThat(resourceType.getValue()).isEqualTo(values[1]);

                break;

            case "repository identifier":
                // expects a string

                assertThat(dataCiteJSON.getRepositoryIdentifier()).isEqualTo(values[0]);
                break;

            case "publisher":
                // expects a string

                assertThat(dataCiteJSON.getPublisher()).isEqualTo(values[0]);
                break;

            case "year":
                // expects a year (e. g. 2012) as int

                assertThat((int) dataCiteJSON.getPublicationYear()).isEqualTo(values[0]);
                break;

            default:
                fail("Requested unknown DataCite property: " + propertyName);
        }

        return self();
    }

    // step method
    public ThenResultingDataCiteProperties the_DataCite_list_property_$_contains_$(
        String listPropertyName,
        Object... values
    )
    {
        switch (listPropertyName) {

            case "formats":
                // expects string with the format (e.g. mimetype "application/json")

                assertThat(values[0]).isIn(dataCiteJSON.getFormats());
                break;

            case "subjects":
                // expects one string, that is compared to the getValue() of Subject

                assertThat(values[0]).isIn(dataCiteJSON.getSubjects().stream().map(Subject::getValue).toArray());
                break;

            case "research disciplines":
                // expects one of the ResearchDisciplineConstants

                assertThat(values[0]).isIn(dataCiteJSON.getResearchDisciplines());
                break;

            case "weblinks":
                // expects one WebLinkType and one URL string

                Predicate<WebLink> webLinkPredicate = wl -> wl.getType() == values[0] & wl.getUrl() == values[1];

                assertThat(dataCiteJSON.getWebLinks().stream().anyMatch(webLinkPredicate));

                break;

            case "descriptions":
                // expects one DescriptionType and one description string

                Predicate<Description> descriptionPredicate = d -> d.getType() == values[0] & d.getValue() == values[1];

                assertThat(dataCiteJSON.getDescriptions().stream().anyMatch(descriptionPredicate));

                break;

            case "creators":
                // expects one NameType and one name string

                assertThat(dataCiteJSON.getCreators().stream().map(Creator::getName).filter(x -> x
                                                                                            .getNameType() == values[0] & x.getValue() == values[1]).count()).isPositive();
                break;

            case "contributors": // expects one ContributorType and one name string

                Predicate<Contributor> contributorPredicate = c -> c.getType() == values[0] & c.getName()
                                                              .getValue() == values[1];

                assertThat(dataCiteJSON.getContributors().stream().anyMatch(contributorPredicate));

                break;

            case "titles":
                // expects on title string

                assertThat(values[0]).isIn(dataCiteJSON.getTitles().stream().map(Title::getValue).toArray());

                break;

            case "dates":
                // expects a string representation of a date

                assertThat(values[0]).isIn(dataCiteJSON.getDates().stream().map(AbstractDate::getValue).toArray());
                break;

            case "geolocations": // expects three doubles: latitude, longitude and elevation

                Predicate<Point> pointPredicate = p -> values.equals(Arrays.asList(p.getLatitude(),
                                                                                   p.getLongitude(),
                                                                                   p.getElevation()));

                assertThat(dataCiteJSON.getGeoLocations().stream().map(x -> (Point) x.getPoint().getCoordinates())
                           .anyMatch(pointPredicate));

                break;

            case "research data list":
                // expects 3 strings: file url, label & format (e.g. "application/json")

                Predicate<ResearchData> researchDataPredicate = rd -> values.equals(Arrays.asList(rd.getUrl(),
                                                                                    rd.getLabel(),
                                                                                    rd.getType()));

                assertThat(dataCiteJSON.getResearchDataList().stream().anyMatch(researchDataPredicate));

                break;

            default:
                fail("Requested unknown DataCite list property: " + listPropertyName);
        }

        return self();
    }
}