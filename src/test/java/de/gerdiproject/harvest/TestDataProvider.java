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
package de.gerdiproject.harvest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * Non-instantiable utility class providing test data for the various scenario
 * steps in form of strings containing JSON responses (as delivered by
 * OceanTEA).
 *
 * There are access methods for JSON strings relating to
 * {@linkplain AllTimeSeriesResponse }, {@linkplain AllDataTypesResponse} and
 * {@linkplain TimeSeriesDatasetResponse}.
 *
 * The test data itself is read from resource files, cached in HashMaps and made
 * accessible through the file name (without .json extension).
 *
 *
 * @author Ingo Thomsen
 *
 */
public final class TestDataProvider
{

    // the resource directors containing the JSON files
    private static final String baseDir                         = "json/";
    private static final String dirDataCiteDocuments            = baseDir + "data_cite_documents/";
    private static final String dirAllDataTypes                 = baseDir + "all_data_types/";
    private static final String dirDatasets                     = baseDir + "time_series_datasets/";
    private static final String dirAllTimeSeries                = baseDir + "all_time_series/";
    private static final String dirTimeSeriesWithSingleDatasets = dirAllTimeSeries + "all_time_series_with_single_dataset_each/";

    // class loader for accessing the resource dirs and files
    private static ClassLoader classLoader = TestDataProvider.class.getClassLoader();

    // A Gson object (for creating DataCiteJson objects)
    private static Gson gson = getGson();

    // the HashMaps containing the JSON strings
    private static HashMap<String, String> allDataTypesJSONStrings  = assembleAllDataTypesJSONStrings();
    private static HashMap<String, String> allTimeSeriesJSONStrings = assembleAllTimeSeriesJSONStrings();
    private static HashMap<String, String> datasetJSONStrings       = assembleDatasetJSONStrings();
    private static HashMap<String, String> dataCiteJsonStrings      = assembleDataCiteJsonStrings();

    /**
     * Return a allDataTypes JSON response
     *
     * @param name Name of the JSON response
     *
     * @return JSON string
     */
    public static String getAllDataTypesJSON(String name)
    {
        if (!allDataTypesJSONStrings.containsKey(name))
            throw new RuntimeException("Expected JSON string '" + name + "' not present!");

        return allDataTypesJSONStrings.get(name);
    }

    /**
     * Return a allTimeSeries JSON response
     *
     * @param name Name of the JSON response
     *
     * @return JSON string
     */
    public static String getAllTimeSeriesJSON(String name)
    {
        if (!allTimeSeriesJSONStrings.containsKey(name))
            throw new RuntimeException("Expected JSON string '" + name + "' not present!");

        return allTimeSeriesJSONStrings.get(name);
    }

    /**
     * Return a timeSeriesDataset JSON response
     *
     * @param name Name of the JSON response
     *
     * @return JSON string
     */
    public static String getTimeSeriesDatasetJSON(String name)
    {
        if (!datasetJSONStrings.containsKey(name))
            throw new RuntimeException("Expected JSON string '" + name + "' not present!");

        return datasetJSONStrings.get(name);
    }

    /**
     * Returns randomly one of the JSON allTimeSeries responses.
     *
     * @return JSON string
     */
    public static String getRandomAllTimeSeriesJSON()
    {
        int randomIndex = (int)(Math.random() * allTimeSeriesJSONStrings.size());
        return allTimeSeriesJSONStrings.values().stream().skip(randomIndex).findFirst().get();
    };

    /**
     * Return a DataCiteJson object
     *
     * @param name Name of the DataCiteJson string to create the object from
     *
     * @return DataCiteJson document created from JSON string
     */
    public static DataCiteJson getExpectedtDataCiteJSON(String name)
    {
        if (!dataCiteJsonStrings.containsKey(name))
            throw new RuntimeException("Expected JSON string '" + name + "' not present!");

        return gson.fromJson(dataCiteJsonStrings.get(name), DataCiteJson.class);
    }

    /**
     * private constructor to enforce non-instantiability
     */
    private TestDataProvider()
    {
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

    /**
     * Private helper to get the content of a resource as string (read as input
     * stream)
     *
     * @param filePath file path (relative to the resource folder)
     * @return string content of the file
     */
    private static String getResourceFileAsString(String filePath)
    {
        InputStream is = classLoader.getResourceAsStream(filePath);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        return br.lines().collect(Collectors.joining());
    }

    /**
     * Private helper to read all JSON files from a resource directory and add each
     * string content to a given HashMap using the file name as key - with the .json
     * extension removed (case-insensitive).
     *
     *
     * @param mapping target HashMap to store the file contents in
     * @param dirWithJSONFiles directory (relative to the resource folder)
     *            containing .json files
     */
    private static void addJSONStringsFromFilesToHashMap(HashMap<String, String> mapping, String dirWithJSONFiles)
    {

        File dir = new File(classLoader.getResource(dirWithJSONFiles).getPath());

        for (File file : dir.listFiles()) {

            String fileName = file.getName();
            String name = fileName.replaceAll("(?i).json", "");

            // add to HashMap if the file had a proper extension
            if (fileName != name)
                mapping.put(name, getResourceFileAsString(dirWithJSONFiles + fileName));
        }
    }

    /**
     * Private helper to create a HashMap with the allDataTypes responses - read
     * from JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all allDataTypes responses
     */
    private static HashMap<String, String> assembleAllDataTypesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(dirAllDataTypes + "all.json"));

        return mapping;
    }

    /**
     * Private helper to create a HashMap with the allTimeSeriesResponses responses
     * - read from JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all allTimeSeriesResponses responses
     */
    private static HashMap<String, String> assembleAllTimeSeriesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(dirAllTimeSeries + "all.json"));
        addJSONStringsFromFilesToHashMap(mapping, dirTimeSeriesWithSingleDatasets);

        return mapping;
    }

    /**
     * Private helper to create a HashMap with the dataset responses - read from
     * JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all dataset responses
     */
    private static HashMap<String, String> assembleDatasetJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        addJSONStringsFromFilesToHashMap(mapping, dirDatasets);

        return mapping;
    }

    /**
     * Private helper to create a HashMap with with the strings - read from JSON
     * files - that represent DataCiteJson documents with the file name (w/o
     * extension) used as key.
     *
     * @return HashMap containing all DataCiteJson strings
     */
    private static HashMap<String, String> assembleDataCiteJsonStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        addJSONStringsFromFilesToHashMap(mapping, dirDataCiteDocuments);

        return mapping;
    }
}