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
package de.gerdiproject.harvest.bdd.stages.then;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import de.gerdiproject.harvest.IDocument;

/**
 * A Then stage testing general properties - independent of the actually
 * harvested dataset(s) - of the resulting {@linkplain IDocument}s
 *
 * @author Ingo Thomsen
 */
public class ThenResultingIDocuments extends Stage<ThenResultingIDocuments>
{
    @ExpectedScenarioState
    List<IDocument> resultingIDocuments;

    public ThenResultingIDocuments there_is_exactly_one_resulting_document()

    {
        assertThat(resultingIDocuments.size()).isOne();
        return self();
    }
}