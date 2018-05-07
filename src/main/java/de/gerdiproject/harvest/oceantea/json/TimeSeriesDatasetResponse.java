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
package de.gerdiproject.harvest.oceantea.json;

import java.util.List;

/**
 * This class represents a JSON response to an actual request for time series
 * data, for example:
 *
 * http://maui.se.informatik.uni-kiel.de:9090/timeseries/scalar/POS434-156/fluorescence/215
 *
 * The JSON data is represented as a list containing the pairs of time offsets
 * (in seconds) and the corresponding values.
 *
 * These pairs themselves are lists with the fixed length of 2. The value can be
 * missing (= "NA"), therefore String is used as type.
 *
 * @author Ingo Thomsen
 */
public final class TimeSeriesDatasetResponse
{

    // This field is directly populated by GSON - accessed by name via Reflection
    private List<List<String>> data;


    //
    // Getter and Setter
    //
    public List<List<String>> getListOfPairsOfTimeOffsetAndValue()
    {
        return data;
    }

    public void setListOfPairsOfTimeOffsetAndValue(List<List<String>> data)
    {
        this.data = data;
    }

}