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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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
    private static final String BASE_DIR               = "json/";
    private static final String DATACITE_DOC_DIR       = BASE_DIR + "data_cite_documents/";
    private static final String ALL_DATA_TYPES_DIR     = BASE_DIR + "all_data_types/";
    private static final String DATASETS_DIR           = BASE_DIR + "time_series_datasets/";
    private static final String ALL_TIME_SERIES_DIR    = BASE_DIR + "all_time_series/";
    private static final String SINGLE_TIME_SERIES_DIR = ALL_TIME_SERIES_DIR + "all_time_series_with_single_dataset_each/";
    private static final String JSON_MISSING           = "Expected JSON string '%s' not present!";

    // class loader for accessing the resource directories and files
    private static final ClassLoader CLASS_LOADER = TestDataProvider.class.getClassLoader();

    // A Gson object (for creating DataCiteJson objects)
    private static final Gson GSON = getGson();

    // the HashMaps containing the JSON strings
    private static final Map<String, String> ALL_DATA_TYPES_JSON_STRINGS  = assembleAllDataTypesJSONStrings();
    private static final Map<String, String> ALL_TIME_SERIES_JSON_STRINGS = assembleAllTimeSeriesJSONStrings();
    private static final Map<String, String> DATASETS_JSON_STRINGS        = assembleDatasetJSONStrings();
    private static final Map<String, String> DATACITE_JSON_STRINGS        = assembleDataCiteJsonStrings();

    /**
     * Return a allDataTypes JSON response
     *
     * @param name Name of the JSON response
     *
     * @return JSON string
     */
    public static String getAllDataTypesJSON(String name)
    {
        if (!ALL_DATA_TYPES_JSON_STRINGS.containsKey(name))
            throw new RuntimeException(String.format(JSON_MISSING, name));

        return ALL_DATA_TYPES_JSON_STRINGS.get(name);
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
        if (!ALL_TIME_SERIES_JSON_STRINGS.containsKey(name))
            throw new RuntimeException(String.format(JSON_MISSING, name));

        return ALL_TIME_SERIES_JSON_STRINGS.get(name);
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
        if (!DATASETS_JSON_STRINGS.containsKey(name))
            throw new RuntimeException(String.format(JSON_MISSING, name));

        return DATASETS_JSON_STRINGS.get(name);
    }

    /**
     * Returns randomly one of the JSON allTimeSeries responses.
     *
     * @return JSON string
     */
    public static String getRandomAllTimeSeriesJSON()
    {
        int randomIndex = (int)(Math.random() * ALL_TIME_SERIES_JSON_STRINGS.size());
        return ALL_TIME_SERIES_JSON_STRINGS.values().stream().skip(randomIndex).findFirst().get();
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
        if (!DATACITE_JSON_STRINGS.containsKey(name))
            throw new RuntimeException(String.format(JSON_MISSING, name));

        return GSON.fromJson(DATACITE_JSON_STRINGS.get(name), DataCiteJson.class);
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
        InputStream is = CLASS_LOADER.getResourceAsStream(filePath);

        String resourceFileString;

        try
            (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            resourceFileString = br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            resourceFileString = null;
        }

        return resourceFileString;
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
    private static void addJSONStringsFromFilesToHashMap(Map<String, String> mapping, String dirWithJSONFiles)
    {

        final File dir = new File(CLASS_LOADER.getResource(dirWithJSONFiles).getPath());
        final File[] filesInDir = dir.listFiles();

        if (filesInDir != null)
            for (File file : filesInDir) {

                String fileName = file.getName();
                String name = fileName.replaceAll("(?i).json", "");

                // add to HashMap if the file had a proper extension
                if (!fileName.equals(name))
                    mapping.put(name, getResourceFileAsString(dirWithJSONFiles + fileName));
            }
    }

    /**
     * Private helper to create a HashMap with the allDataTypes responses - read
     * from JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all allDataTypes responses
     */
    private static Map<String, String> assembleAllDataTypesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(ALL_DATA_TYPES_DIR + "all.json"));

        return mapping;
    }

    /**
     * Private helper to create a HashMap with the allTimeSeriesResponses responses
     * - read from JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all allTimeSeriesResponses responses
     */
    private static Map<String, String> assembleAllTimeSeriesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(ALL_TIME_SERIES_DIR + "all.json"));
        addJSONStringsFromFilesToHashMap(mapping, SINGLE_TIME_SERIES_DIR);

        return mapping;
    }

    /**
     * Private helper to create a HashMap with the dataset responses - read from
     * JSON files - using the file names (w/o extension) as key.
     *
     * @return HashMap containing all dataset responses
     */
    private static Map<String, String> assembleDatasetJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        addJSONStringsFromFilesToHashMap(mapping, DATASETS_DIR);

        return mapping;
    }

    /**
     * Private helper to create a HashMap with with the strings - read from JSON
     * files - that represent DataCiteJson documents with the file name (w/o
     * extension) used as key.
     *
     * @return HashMap containing all DataCiteJson strings
     */
    private static Map<String, String> assembleDataCiteJsonStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        addJSONStringsFromFilesToHashMap(mapping, DATACITE_DOC_DIR);

        return mapping;
    }
}