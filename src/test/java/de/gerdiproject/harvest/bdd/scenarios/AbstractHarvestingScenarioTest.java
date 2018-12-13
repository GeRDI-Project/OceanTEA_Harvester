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
package de.gerdiproject.harvest.bdd.scenarios;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.tngtech.jgiven.junit.ScenarioTest;

import de.gerdiproject.harvest.etl.WhenHarvesting;
import de.gerdiproject.harvest.etls.extractors.TimeSeriesExtractor;

/**
 * This is the base (class) for all scenarios that use the
 * {@linkplain WhenHarvesting} stage. It also prepares the mocking there for the
 * {@linkplain TimeSeriesExtractor} class.
 *
 * @author Ingo Thomsen
 * @param <GivenStage> Given stage with steps used before {@linkplain WhenHarvesting} stage steps
 * @param <ThenStage> Then stage with steps used after {@linkplain WhenHarvesting} stage steps
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TimeSeriesExtractor.class)
abstract public class AbstractHarvestingScenarioTest<GivenStage, ThenStage> extends ScenarioTest<GivenStage, WhenHarvesting, ThenStage>
{}