/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.plugins.binaries.model;

import org.gradle.api.Buildable;
import org.gradle.api.Named;
import org.gradle.api.Project;
import org.gradle.api.DomainObjectSet;

/**
 * Something to be created.
 */
public interface Binary extends Named, Buildable {

    CompileSpec getSpec();

    /**
     * Returns the project that this binary is built by.
     *
     * @deprecated No replacement
     */
    @Deprecated
    Project getProject();
    
    DomainObjectSet<SourceSet> getSourceSets();
}
