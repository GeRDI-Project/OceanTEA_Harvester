/**
 * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de) Licensed under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.gerdiproject.harvest.oceantea.json;

import lombok.Data;

/**
 * This class represents a JSON object that is part of an
 * {@linkplain AllDataTypesResponse}: The print name and the unit of a data
 * type.
 *
 * @author Ingo Thomsen
 */
@Data
public final class DataTypeResponse
{
    private String printName;
    private String unit;


    /**
     * Constructor
     *
     * @param printName the string representation of the data type name
     * @param unit the string representation of the data type unit
     */
    public DataTypeResponse(String printName, String unit)
    {
        this.printName = printName;
        this.unit = unit;
    }
}