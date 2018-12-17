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
package de.gerdiproject.harvest.bdd.stages.then;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.google.gson.Gson;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * This stage is for comparing the DataCite properties of the expected
 * DataCiteJson and the provided DataCiteJson (the first element of the
 * resulting IDocuments). Both is provided via ScenarioState variables.
 * Generally, there are three kinds of {@linkplain DataCiteJson} properties:
 * <ul>
 * <li>"constant": (collection of) values, independent of the harvested time
 * series dataset
 * <li>"variable": (collection of) values, solely dependent of the harvested
 * time series dataset
 * <li>"partly variable": collection of values with elements dependent &
 * independent of the harvested time series dataset. For this there is a method
 * to check if the "actual DataCite fields" contain the "expected" ones.
 * </ul>
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
    private static final Gson GSON = GsonUtils.createGerdiDocumentGsonBuilder().setPrettyPrinting().create();


    // step method
    public ThenResultingDataCiteProperties all_constant_DataCite_properties_equal_those_in_expected_DataCiteJSON()
    {
        assertEqualDataCiteField(DataCiteJson::getCreators);
        assertEqualDataCiteField(DataCiteJson::getPublisher);
        assertEqualDataCiteField(DataCiteJson::getResourceType);
        assertEqualDataCiteField(DataCiteJson::getContributors);
        assertEqualDataCiteField(DataCiteJson::getFormats);
        assertEqualDataCiteField(DataCiteJson::getRepositoryIdentifier);
        assertEqualDataCiteField(DataCiteJson::getResearchDisciplines);

        return self();
    }


    // step method
    public ThenResultingDataCiteProperties all_partly_variable_DataCite_properties_contain_those_in_expected_DataCiteJSON()
    {
        assertExpectedDataCiteFieldArePresent(DataCiteJson::getSubjects);
        assertExpectedDataCiteFieldArePresent(DataCiteJson::getDescriptions);
        assertExpectedDataCiteFieldArePresent(DataCiteJson::getWebLinks);

        return self();
    }


    // step method
    public ThenResultingDataCiteProperties all_variable_DataCite_properties_equal_those_in_expected_DataCiteJSON()
    {
        assertEqualDataCiteField(DataCiteJson::getPublicationYear);
        assertEqualDataCiteField(DataCiteJson::getDates);
        assertEqualDataCiteField(DataCiteJson::getGeoLocations);
        assertEqualDataCiteField(DataCiteJson::getTitles);
        assertEqualDataCiteField(DataCiteJson::getResearchDataList);

        return self();
    }


    /**
     * Private method to make the first document of the list resulting IDocuments
     * available to the step methods in this state class. Because of
     * {@linkplain BeforeStage} it is called before any other stage method.
     */
    @BeforeStage
    private void extractDataCiteJSONFromFirstEntry()
    {
        resultingDataCiteJson = (DataCiteJson) resultingIDocuments.get(0);
    }


    /**
     * Function to check equality of DataCite fields between an expected and the
     * actually created DataCite document, using a given {@linkplain DataCiteJson}
     * getter.
     *
     * @param getter {@linkplain DataCiteJson} getter
     * @param <T> type of the DataCite field returned by the getter
     *
     */
    private <T> void assertEqualDataCiteField(Function<DataCiteJson, T> getter)
    {
        T expected = getter.apply(expectedDataCiteJson);
        T actual = getter.apply(resultingDataCiteJson);

        assertThat(actual).as("The resulting DataCite field %s does not match the expected %s",
                              GSON.toJson(actual),
                              GSON.toJson(expected)).isEqualTo(expected);
    }


    /**
     * Function to check for a given DataCite field - which is a collection (either
     * Set or List) - that all expected collection items are in the actually created
     * collection. The field is accessed using a given {@linkplain DataCiteJson}
     * getter.
     *
     * @param getter {@linkplain DataCiteJson} getter for a collection
     * @param <T> element type of the collection returned by the getter
     */
    private <T> void assertExpectedDataCiteFieldArePresent(Function<DataCiteJson, Collection<T>> getter)
    {
        Collection<T> expected = getter.apply(expectedDataCiteJson);
        Collection<T> actual = getter.apply(resultingDataCiteJson);

        expected.forEach(e -> assertThat(e).as("The resulting DataCite field %s does not contain the expected %s",
                                               GSON.toJson(actual),
                                               GSON.toJson(expected)).isIn(actual));
    }
}