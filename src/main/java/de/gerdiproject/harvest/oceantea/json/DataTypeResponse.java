/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package de.gerdiproject.harvest.oceantea.json;

/**
 * This class represents a JSON object that is part of an
 * {@linkplain AllDataTypesResponse}: The print name and the unit of a data type.
 *
 * @author Ingo Thomsen
 */
public final class DataTypeResponse
{
    private String printName;
    private String unit;

    /**
     * Constructor
     *
     * @param printName
     *            the string representation of the data type name
     * @param unit
     *            the string representation of the data type unit
     */
    public DataTypeResponse(String printName, String unit)
    {
        this.printName = printName;
        this.unit = unit;
    }

    //
    // Getter and Setter
    //
    public String getPrintName()
    {
        return printName;
    }

    public void setPrintName(String value)
    {
        this.printName = value;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String value)
    {
        this.unit = value;
    }
}