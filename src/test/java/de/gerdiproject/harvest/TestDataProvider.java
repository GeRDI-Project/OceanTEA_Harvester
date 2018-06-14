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

import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesDatasetResponse;

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
    private static final String dirDataTypes                    = baseDir + "all_data_types/";
    private static final String dirTimeSeries                   = baseDir + "all_time_series/";
    private static final String dirDatasets                     = baseDir + "time_series_datasets/";
    private static final String dirTimeSeriesWithSingleDatasets = dirTimeSeries + "all_time_series_with_single_dataset_each/";

    // class loader for accessing the resource dirs and files
    private static ClassLoader classLoader = TestDataProvider.class.getClassLoader();

    // the HashMaps containing the JSON response strings
    private static HashMap<String, String> allDataTypesJSONStrings  = assembleAllDataTypesJSONStrings();
    private static HashMap<String, String> allTimeSeriesJSONStrings = assembleAllTimeSeriesJSONStrings();
    private static HashMap<String, String> datasetJSONStrings       = assembleDatasetJSONStrings();

    /**
     * private constructor to enforce non-instantiability
     */
    private TestDataProvider()
    {
    }

    /**
     * Return a allDataTypes JSON response
     *
     * @param name Name of the JSON response
     *
     * @return JSON string
     */
    public static String getAllDataTypesJSON(String name)
    {
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

    //
    // private helper method to read a resource file (as input stream) and return
    // its content as String
    //
    private static String getResourceFileAsString(String filePath)
    {
        InputStream is = classLoader.getResourceAsStream(filePath);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        return br.lines().collect(Collectors.joining());
    }

    //
    // private helper method to read all files from a resource directory & add each
    // string content to a given HashMap using the file name (w/o extension) as key
    //
    private static void addJSONStringsFromFilesToHashMap(HashMap<String, String> mapping, String dirWithJSONFiles)
    {

        File dir = new File(classLoader.getResource(dirWithJSONFiles).getPath());

        for (File file : dir.listFiles()) {

            String fileName = file.getName();
            String name = fileName.replaceAll(".json", "");

            mapping.put(name, getResourceFileAsString(dirWithJSONFiles + fileName));
        }
    }

    //
    // private helper method to assemble and save the JSON dataType responses from
    // the respective resource dir
    //
    private static HashMap<String, String> assembleAllDataTypesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(dirDataTypes + "all.json"));

        return mapping;
    }

    //
    // private helper method to assemble and save the JSON allTimeSeries responses
    // from the resource dirs
    //
    private static HashMap<String, String> assembleAllTimeSeriesJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("all", getResourceFileAsString(dirTimeSeries + "all.json"));
        addJSONStringsFromFilesToHashMap(mapping, dirTimeSeriesWithSingleDatasets);

        return mapping;
    }

    //
    // private helper method to assemble and save the JSON dataset responses from
    // the respective resource dir
    //
    private static HashMap<String, String> assembleDatasetJSONStrings()
    {
        HashMap<String, String> mapping = new HashMap<>();

        addJSONStringsFromFilesToHashMap(mapping, dirDatasets);

        return mapping;
    }

}
