/**
 * Copyright © 2018 Ingo Thomsen, Robin Weiss (http://www.gerdi-project.de)
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
package de.gerdiproject.harvest.etls.extractors;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import com.google.gson.Gson;

import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDownloaderConstants;
import de.gerdiproject.harvest.oceantea.json.AllDataTypesResponse;
import de.gerdiproject.harvest.oceantea.json.AllTimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.json.DataTypeResponse;
import de.gerdiproject.harvest.oceantea.json.TimeSeriesResponse;
import de.gerdiproject.harvest.oceantea.utils.TimeSeries;
import de.gerdiproject.harvest.utils.data.HttpRequester;


/**
 * Extracts a list of {@linkplain TimeSeries} objects that describe the metadata
 * for each time series dataset.
 *
 * @author Ingo Thomsen, Robin Weiss
 */
public class TimeSeriesExtractor extends AbstractIteratorExtractor<TimeSeries>
{
    private final HttpRequester httpRequester = new HttpRequester(new Gson(), StandardCharsets.UTF_8);

    protected Iterator<TimeSeriesResponse> timeSeriesSource;
    protected AllDataTypesResponse dataTypeInfoSource;
    private int timeSeriesCount;


    @Override
    public String getUniqueVersionString()
    {
        // the size should be enough for now, because existing elements never change
        // if the number of timeseries changes, the version should be different
        return String.valueOf(timeSeriesCount);
    }


    @Override
    public void init(final AbstractETL<?, ?> etl)
    {
        super.init(etl);

        httpRequester.setCharset(etl.getCharset());

        // get data type info responses
        this.dataTypeInfoSource = httpRequester.getObjectFromUrl(
                                      OceanTeaTimeSeriesDownloaderConstants.DATATYPES_URL,
                                      AllDataTypesResponse.class);
        this.dataTypeInfoSource.getPotentialDensityAnomaly().setUnit("kg/m^3");

        // get all timeseries responses
        final AllTimeSeriesResponse allTimeSeriesResponse = httpRequester.getObjectFromUrl(
                                                                OceanTeaTimeSeriesDownloaderConstants.TIMESERIES_URL,
                                                                AllTimeSeriesResponse.class);
        this.timeSeriesSource = allTimeSeriesResponse.getAllTimeSeriesResponses().iterator();

        // get expected size of extracted elements
        this.timeSeriesCount = allTimeSeriesResponse.getAllTimeSeriesResponses().size();
    }


    @Override
    public int size()
    {
        return timeSeriesCount;
    }


    @Override
    protected Iterator<TimeSeries> extractAll() throws ExtractorException
    {
        return new TimeSeriesIterator();
    }


    /**
     * Iterator class that takes the next element of the {@linkplain TimeSeriesResponse} {@linkplain Iterator}
     * and enriches it with {@linkplain DataTypeResponse} data.
     *
     * @author Robin Weiss
     */
    private class TimeSeriesIterator implements Iterator<TimeSeries>
    {
        private int index = 0; // NOPMD index explicitly starts at 0


        @Override
        public boolean hasNext()
        {
            return timeSeriesSource.hasNext();
        }


        @Override
        public TimeSeries next()
        {
            final TimeSeriesResponse tsr = timeSeriesSource.next();
            final DataTypeResponse dataTypeInfo = dataTypeInfoSource.getDatatypeResponseByName(tsr.getDataType());

            return new TimeSeries(index++, tsr, dataTypeInfo);
        }

    }


    @Override
    public void clear()
    {
        // nothing to clean up
    }
}
