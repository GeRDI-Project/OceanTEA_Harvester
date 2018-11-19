/**
, * Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de)
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
package de.gerdiproject.harvest.etls;

import de.gerdiproject.harvest.etls.StaticIteratorETL;
import de.gerdiproject.harvest.etls.extractors.TimeSeriesExtractor;
import de.gerdiproject.harvest.etls.transformers.TimeSeriesTransformer;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * Harvester for OceanTEA time series data
 *
 * @author Ingo Thomsen
 */
public class TimeSeriesETL extends StaticIteratorETL<TimeSeries, DataCiteJson>
{
    /**
     * Default constructor, naming the harvester and ensuring one document per
     * harvested entry
     */
    public TimeSeriesETL()
    {
        super(new TimeSeriesExtractor(), new TimeSeriesTransformer());
    }
}
