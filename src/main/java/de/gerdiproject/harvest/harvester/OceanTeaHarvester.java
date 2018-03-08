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
package de.gerdiproject.harvest.harvester;

import java.util.LinkedList;
import java.util.List;

import de.gerdiproject.harvest.harvester.subharvester.TimeSeriesHarvester;

/**
 * The Main Harvester for OceanTEA. It is devised as a
 * {@linkplain AbstractCompositeHarvester}, containing one sub harvester for the
 * time series data.
 *
 * @author Ingo Thomsen
 */
public class OceanTeaHarvester extends AbstractCompositeHarvester
{

    /**
     * default constructor, creating the (at the moment 1) sub harvesters
     */
    public OceanTeaHarvester()
    {
        super(createSubHarvesters());
    }

    /**
     * Creates all sub-harvesters that harvest OceanTEA.
     *
     * @return all required sub-harvesters for OceanTEA
     */
    private static List<AbstractHarvester> createSubHarvesters()
    {
        LinkedList<AbstractHarvester> newSubHarvesters = new LinkedList<>();

        newSubHarvesters.add(new TimeSeriesHarvester());

        return newSubHarvesters;
    }
}