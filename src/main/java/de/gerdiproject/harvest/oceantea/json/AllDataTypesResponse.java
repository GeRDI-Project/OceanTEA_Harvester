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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class represents a JSON response to an "all data types" requests, for
 * example: http://maui.se.informatik.uni-kiel.de:9090/datatypes
 *
 * @author Ingo Thomsen
 */
public final class AllDataTypesResponse
{

    private DataTypeResponse conductivity;
    private DataTypeResponse temperature;
    private DataTypeResponse pressure;
    private DataTypeResponse pH;
    private DataTypeResponse fluorescence;
    private DataTypeResponse turbidity;
    private DataTypeResponse oxygen;
    private DataTypeResponse saturation;
    private DataTypeResponse practicalSalinity;
    private DataTypeResponse absoluteSalinity;
    private DataTypeResponse potentialTemperature;
    private DataTypeResponse conservativeTemperature;
    private DataTypeResponse soundSpeed;
    private DataTypeResponse potentialDensityAnomaly;

    /**
     * Get more information (print name and unit) for a data type. Uses reflection
     * and assumes the standard naming for getter methods.
     *
     * @return a {@linkplain DataTypeResponse} object
     */
    public DataTypeResponse getDatatypeResponseByName(String name)
    {

        Method getterMethod;

        try {
            getterMethod = this.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
            return (DataTypeResponse) getterMethod.invoke(this);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException e) {
            return new DataTypeResponse("unknown", "unknown");
        }

    }

    //
    // Getter and Setter
    //

    public DataTypeResponse getConductivity()
    {
        return conductivity;
    }

    public void setConductivity(DataTypeResponse value)
    {
        this.conductivity = value;
    }

    public DataTypeResponse getTemperature()
    {
        return temperature;
    }

    public void setTemperature(DataTypeResponse value)
    {
        this.temperature = value;
    }

    public DataTypeResponse getPressure()
    {
        return pressure;
    }

    public void setPressure(DataTypeResponse value)
    {
        this.pressure = value;
    }

    public DataTypeResponse getPH()
    {
        return pH;
    }

    public void setPH(DataTypeResponse value)
    {
        this.pH = value;
    }

    public DataTypeResponse getFluorescence()
    {
        return fluorescence;
    }

    public void setFluorescence(DataTypeResponse value)
    {
        this.fluorescence = value;
    }

    public DataTypeResponse getTurbidity()
    {
        return turbidity;
    }

    public void setTurbidity(DataTypeResponse value)
    {
        this.turbidity = value;
    }

    public DataTypeResponse getOxygen()
    {
        return oxygen;
    }

    public void setOxygen(DataTypeResponse value)
    {
        this.oxygen = value;
    }

    public DataTypeResponse getSaturation()
    {
        return saturation;
    }

    public void setSaturation(DataTypeResponse value)
    {
        this.saturation = value;
    }

    public DataTypeResponse getPracticalSalinity()
    {
        return practicalSalinity;
    }

    public void setPracticalSalinity(DataTypeResponse value)
    {
        this.practicalSalinity = value;
    }

    public DataTypeResponse getAbsoluteSalinity()
    {
        return absoluteSalinity;
    }

    public void setAbsoluteSalinity(DataTypeResponse value)
    {
        this.absoluteSalinity = value;
    }

    public DataTypeResponse getPotentialTemperature()
    {
        return potentialTemperature;
    }

    public void setPotentialTemperature(DataTypeResponse value)
    {
        this.potentialTemperature = value;
    }

    public DataTypeResponse getConservativeTemperature()
    {
        return conservativeTemperature;
    }

    public void setConservativeTemperature(DataTypeResponse value)
    {
        this.conservativeTemperature = value;
    }

    public DataTypeResponse getSoundSpeed()
    {
        return soundSpeed;
    }

    public void setSoundSpeed(DataTypeResponse value)
    {
        this.soundSpeed = value;
    }

    public DataTypeResponse getPotentialDensityAnomaly()
    {
        return potentialDensityAnomaly;
    }

    public void setPotentialDensityAnomaly(DataTypeResponse value)
    {
        this.potentialDensityAnomaly = value;
    }
}