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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.Contributor;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.extension.ResearchData;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.abstr.AbstractResearch;
import de.gerdiproject.json.geo.Point;


/**
 * This stage is for comparing the DataCite properties of the expected
 * DataCiteJson and the provided DataCiteJson (the first element of the
 * resulting IDocuments). Both is provided via ScenarioState variables.
 *
 * Generally, there are three kinds of properties:
 *
 * "constant": list or value that is independent of the actual time series
 * dataset
 *
 * "variable": list or value that is only dependent of the time series dataset
 *
 * "partly variable": list with elements dependent & independent of the time
 * series dataset
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

    @ExpectedScenarioState
    DataCiteJson expectedDataCiteJson;

    // The first of resulting IDocuments, which is used for all comparisons
    private DataCiteJson resultingDataCiteJson;

    // A Gson object (for creating JSON for messages)
    private static final Gson gson = getGson();


    // step method (using other step methods)
    public ThenResultingDataCiteProperties all_constant_DataCite_properties_equal_those_in_expected_DataCiteJSON()
    {
        the_contributors_are_as_expected();
        the_creators_are_as_expected();
        the_formats_are_as_expected();
        the_publisher_is_as_expected();
        the_repository_identifier_as_expected();
        the_research_disciplines_are_as_expected();
        the_resource_type_is_as_expected();

        return self();
    }


    // step method (using other step methods)
    public ThenResultingDataCiteProperties all_partly_variable_DataCite_properties_contain_those_in_expected_DataCiteJSON()
    {
        the_descriptions_list_contains_exptected();
        the_subjects_list_contains_exptected();
        the_weblinks_list_contains_expected();

        return self();
    }


    // step method (using other step methods)
    public ThenResultingDataCiteProperties all_variable_DataCite_properties_equal_those_in_expected_DataCiteJSON()
    {
        the_dates_are_as_expected();
        the_geolocations_are_as_exptected();
        the_publication_year_is_as_expected();
        the_research_data_list_is_as_expected();
        the_titles_are_as_expected();

        return self();
    }


    // step method
    public void the_contributors_are_as_expected()
    {
        // comparator that ignores affiliations, name identifiers, family & given name
        Comparator<Contributor> comparator = (Contributor a, Contributor b) -> {
            return (
                a.getType() == b.getType() &&
                a.getName().getNameType() == b.getName().getNameType() &&
                a.getName().getValue().equals(b.getName().getValue())
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getContributors());
    }


    // step method
    public void the_creators_are_as_expected()
    {
        // comparator (ignoring affiliations, name identifiers, family & given name)
        Comparator<Creator> comparator = (Creator a, Creator b) -> {
            return (
                a.getName().getValue().equals(b.getName().getValue()) &&
                a.getName().getNameType().equals(b.getName().getNameType())
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getCreators());
    }


    // step method
    public void the_dates_are_as_expected()
    {
        Comparator<AbstractDate> comparator = (AbstractDate a, AbstractDate b) -> {
            return (
                a.getValue().equals(b.getValue()) &&
                a.getType() == b.getType()
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getDates());
    }


    // step method
    public void the_descriptions_list_contains_exptected()
    {
        Comparator<Description> comparator = (Description a, Description b) -> {
            return (
                a.getType() == b.getType() &&
                a.getValue().equals(b.getValue()) &&
                a.getLang().equals(b.getLang())
            ) ? 0 : 1;
        };

        assertThatResultingListContainsExpected(comparator, (DataCiteJson x) -> x.getDescriptions());
    }


    // step method
    public void the_formats_are_as_expected()
    {
        List<String> resulting = resultingDataCiteJson.getFormats();
        List<String> expected = expectedDataCiteJson.getFormats();

        assertThat(resulting).
        as("The resulting list %s does not the same elements as expteced list %s",
           gson.toJson(resulting),
           gson.toJson(expected)).
        containsAll(expected);
    }


    // step method
    public void the_geolocations_are_as_exptected()
    {
        Comparator<GeoLocation> comparator = (GeoLocation a, GeoLocation b) -> {

            Point pointA = (Point) a.getPoint().getCoordinates();
            Point pointB = (Point) b.getPoint().getCoordinates();

            return (
                a.getPlace().equals(b.getPlace()) &&
                pointA.getLongitude() == pointB.getLongitude() &&
                pointA.getLatitude() == pointB.getLatitude() &&
                pointA.getElevation() == pointB.getElevation()
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getGeoLocations());
    }


    // step method
    public void the_publication_year_is_as_expected()
    {
        assertThat(resultingDataCiteJson.getPublicationYear()).
        as("Publication Year").
        isEqualTo(expectedDataCiteJson.getPublicationYear());
    }


    // step method
    public void the_publisher_is_as_expected()
    {
        assertThat(resultingDataCiteJson.getPublisher()).
        as("Publisher").
        isEqualTo(expectedDataCiteJson.getPublisher());
    }


    // step method
    public void the_repository_identifier_as_expected()
    {
        assertThat(resultingDataCiteJson.getRepositoryIdentifier()).
        as("Repository Identifier").
        isEqualTo(expectedDataCiteJson.getRepositoryIdentifier());
    }


    // step method
    public void the_research_data_list_is_as_expected()
    {
        Comparator<ResearchData> comparator = (ResearchData a, ResearchData b) -> {
            return (
                a.getIdentifier().equals(b.getIdentifier()) &&
                a.getLabel().equals(b.getLabel()) && a.getType().equals(b.getType()) &&
                a.getUrl().equals(b.getUrl())
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getResearchDataList());
    }


    // step method
    public void the_research_disciplines_are_as_expected()
    {
        Comparator<AbstractResearch> comparator = (AbstractResearch a, AbstractResearch b) -> {
            return (
                a.getAreaName().equals(b.getAreaName()) &&
                a.getCategoryName().equals(b.getCategoryName()) &&
                a.getDisciplineName().equals(b.getDisciplineName()) &&
                a.getRbnr() == b.getRbnr()
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getResearchDisciplines());
    }


    // step method
    public void the_resource_type_is_as_expected()
    {
        ResourceType resultingResourceType = resultingDataCiteJson.getResourceType();
        ResourceType exptectedResourceType = expectedDataCiteJson.getResourceType();

        assertThat(resultingResourceType.getValue()).isEqualTo(exptectedResourceType.getValue());
        assertThat(resultingResourceType.getGeneralType()).isEqualTo(exptectedResourceType.getGeneralType());
    }


    // step method
    public void the_subjects_list_contains_exptected()
    {
        // comparator (ignoring lang, schemeURI, subjectScheme, valueURI)
        Comparator<Subject> comparator = (Subject a, Subject b) -> {
            return a.getValue().equals(b.getValue()) ? 0 : 1;
        };

        assertThatResultingListContainsExpected(comparator, (DataCiteJson x) -> x.getSubjects());
    }


    // step method
    public void the_titles_are_as_expected()
    {
        Comparator<Title> comparator = (Title a, Title b) -> {
            return (
                a.getLang().equals(b.getLang()) &&
                a.getValue().equals(b.getValue()) &&
                a.getType() == b.getType()
            ) ? 0 : 1;
        };

        assertThatResultingAndExpectedListAreEqual(comparator, (DataCiteJson x) -> x.getTitles());
    }


    // step method
    public void the_weblinks_list_contains_expected()
    {
        Comparator<WebLink> comparator = (WebLink a, WebLink b) -> {
            return (
                a.getType() == b.getType() &&
                a.getUrl().equals(b.getUrl())
            ) ? 0 : 1;
        };

        assertThatResultingListContainsExpected(comparator, (DataCiteJson x) -> x.getWebLinks());
    }


    /**
     * Private generic helper to assert that a list property of the resulting and
     * expected DataCiteJson contain the same elements.
     *
     * @param comparator For comparing the list elements
     * @param getter For accessing the lists from DataCiteJson resulting & expected
     */
    private <T> void assertThatResultingAndExpectedListAreEqual(
        Comparator<T> comparator,
        Function<DataCiteJson, List<T>> getter
    )
    {
        List<T> resulting = getter.apply(resultingDataCiteJson);
        List<T> expected = getter.apply(expectedDataCiteJson);

        assertThat(resulting).as("The resulting list %s does not the same elements as expteced list %s",
                                 gson.toJson(resulting),
                                 gson.toJson(expected)).usingElementComparator(comparator).hasSameElementsAs(expected);
    }


    /**
     * Private generic helper to assert that a list property of the resulting
     * DataCiteJson contains all the elements of the respective expected list. This
     * is used for the partly variable list properties.
     *
     * @param comparator For comparing the list elements
     * @param getter For accessing the lists from DataCiteJson resulting & expected
     */
    private <T> void assertThatResultingListContainsExpected(
        Comparator<T> comparator,
        Function<DataCiteJson, List<T>> getter
    )
    {
        List<T> resulting = getter.apply(resultingDataCiteJson);
        List<T> expected = getter.apply(expectedDataCiteJson);

        assertThat(resulting).as("The resulting list %s does not conatain all elements of expteced list %s",
                                 gson.toJson(resulting),
                                 gson.toJson(expected)).usingElementComparator(comparator).containsAll(expected);
    }


    /**
     * Private method to make the first document of the list resulting IDocuments
     * available to the step methods in this state class. This is called before any
     * stage methods.
     */
    @BeforeStage
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void extractDataCiteJSONFromFirstEntry()
    {
        resultingDataCiteJson = (DataCiteJson) resultingIDocuments.get(0);
    }


    /**
     * Initialize Gson and return a PrettyGson object
     *
     * @return Gson object
     */
    private static Gson getGson()
    {
        GsonUtils.init(new GsonBuilder());
        return GsonUtils.getPrettyGson();
    }
}