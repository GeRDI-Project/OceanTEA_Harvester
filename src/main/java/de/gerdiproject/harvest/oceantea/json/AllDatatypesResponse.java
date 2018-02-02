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

import java.lang.reflect.Method;

/**
 * This class represents a JSON response to a "all datatypes" requests, for
 * example: http://maui.se.informatik.uni-kiel.de:9090/datatypes
 *
 * @author Ingo Thomsen
 */
public final class AllDatatypesResponse {

	private DatatypeResponse conductivity;
	private DatatypeResponse temperature;
	private DatatypeResponse pressure;
	private DatatypeResponse pH;
	private DatatypeResponse fluorescence;
	private DatatypeResponse turbidity;
	private DatatypeResponse oxygen;
	private DatatypeResponse saturation;
	private DatatypeResponse practicalSalinity;
	private DatatypeResponse absoluteSalinity;
	private DatatypeResponse potentialTemperature;
	private DatatypeResponse conservativeTemperature;
	private DatatypeResponse soundSpeed;
	private DatatypeResponse potentialDensityAnomaly;

	public DatatypeResponse getDatatypeResponseByName(String name) {

		try {
			Method getterMethod = this.getClass()
					.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
			return (DatatypeResponse) getterMethod.invoke(this);

		} catch (Exception e) {

			return new DatatypeResponse("unknown", "unknown");
		}
	}

	//
	// Setter and Getter
	//
	public DatatypeResponse getConductivity() {
		return conductivity;
	}

	public void setConductivity(DatatypeResponse value) {
		this.conductivity = value;
	}

	public DatatypeResponse getTemperature() {
		return temperature;
	}

	public void setTemperature(DatatypeResponse value) {
		this.temperature = value;
	}

	public DatatypeResponse getPressure() {
		return pressure;
	}

	public void setPressure(DatatypeResponse value) {
		this.pressure = value;
	}

	public DatatypeResponse getPH() {
		return pH;
	}

	public void setPH(DatatypeResponse value) {
		this.pH = value;
	}

	public DatatypeResponse getFluorescence() {
		return fluorescence;
	}

	public void setFluorescence(DatatypeResponse value) {
		this.fluorescence = value;
	}

	public DatatypeResponse getTurbidity() {
		return turbidity;
	}

	public void setTurbidity(DatatypeResponse value) {
		this.turbidity = value;
	}

	public DatatypeResponse getOxygen() {
		return oxygen;
	}

	public void setOxygen(DatatypeResponse value) {
		this.oxygen = value;
	}

	public DatatypeResponse getSaturation() {
		return saturation;
	}

	public void setSaturation(DatatypeResponse value) {
		this.saturation = value;
	}

	public DatatypeResponse getPracticalSalinity() {
		return practicalSalinity;
	}

	public void setPracticalSalinity(DatatypeResponse value) {
		this.practicalSalinity = value;
	}

	public DatatypeResponse getAbsoluteSalinity() {
		return absoluteSalinity;
	}

	public void setAbsoluteSalinity(DatatypeResponse value) {
		this.absoluteSalinity = value;
	}

	public DatatypeResponse getPotentialTemperature() {
		return potentialTemperature;
	}

	public void setPotentialTemperature(DatatypeResponse value) {
		this.potentialTemperature = value;
	}

	public DatatypeResponse getConservativeTemperature() {
		return conservativeTemperature;
	}

	public void setConservativeTemperature(DatatypeResponse value) {
		this.conservativeTemperature = value;
	}

	public DatatypeResponse getSoundSpeed() {
		return soundSpeed;
	}

	public void setSoundSpeed(DatatypeResponse value) {
		this.soundSpeed = value;
	}

	public DatatypeResponse getPotentialDensityAnomaly() {
		return potentialDensityAnomaly;
	}

	public void setPotentialDensityAnomaly(DatatypeResponse value) {
		this.potentialDensityAnomaly = value;
	}
}