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
package de.gerdiproject.harvest.oceantea.constants;

/**
 * Non-instantiable utility class with constants for downloading metadata and data from OceanTEA.
 *
 * @author Ingo Thomsen
 */
public final class OceanTeaTimeSeriesDownloaderConstants
{

    //
    // static URLs
    //
    public static final String TIMESERIES_URL = OceanTeaTimeSeriesDataCiteConstants.BASE_URL + "timeseries/";
    public static final String DATATYPES_URL = OceanTeaTimeSeriesDataCiteConstants.BASE_URL + "datatypes/";
    public static final String NON_UNIVARIANT_TIME_SERIES = "adcp";

    // template string
    public static final String DATASET_DOWNLOAD_URL = TIMESERIES_URL + "%s/%s/%s/%.0f";

    /**
     * private constructor to enforce non-instantiability
     */
    private OceanTeaTimeSeriesDownloaderConstants()
    {
    }

}
