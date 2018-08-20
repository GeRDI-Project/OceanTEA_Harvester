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
package de.gerdiproject.harvest.bdd.tags;

import java.lang.annotation.Annotation;

import com.tngtech.jgiven.annotation.TagDescriptionGenerator;
import com.tngtech.jgiven.config.TagConfiguration;

/**
 * A static class that generates for other Tags the HTML link to a Jira issue
 */
public class JiraLinkDescriptionGenerator implements TagDescriptionGenerator
{
    private static final String LINK_TEST_TEMPLATE = "<a href='https://tasks.gerdi-project.de/browse/%s'>%s (→ JIRA)</a>";

    @Override
    public String generateDescription(TagConfiguration tagConfiguration, Annotation annotation, Object tagValue)
    {
        return String.format(LINK_TEST_TEMPLATE, tagValue, tagConfiguration.getName());
    }
}