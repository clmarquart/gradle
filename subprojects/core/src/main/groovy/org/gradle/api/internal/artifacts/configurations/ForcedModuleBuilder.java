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

package org.gradle.api.internal.artifacts.configurations;

import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.internal.artifacts.DefaultResolvedModuleId;
import org.gradle.util.ConfigureUtil;
import org.gradle.util.GUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * by Szczepan Faber, created at: 10/11/11
 */
public class ForcedModuleBuilder {

    public Set<ModuleIdentifier> build(Object notation) {
        assert notation != null : "notation cannot be null";
        Set<ModuleIdentifier> out = new LinkedHashSet<ModuleIdentifier>();
        Collection notations = GUtil.normalize(notation);
        for (Object n : notations) {
            out.add(parseSingleNotation(n));
        }

        return out;
    }

    private ModuleIdentifier parseSingleNotation(Object notation) {
        if (notation instanceof ModuleIdentifier) {
            return (ModuleIdentifier) notation;
        } else if (notation instanceof Map) {
            return parseMap((Map) notation);
        } else {
            return parseString(notation.toString());
        }
    }

    private ModuleIdentifier parseMap(Map notation) {
        ModuleIdentifier out = new DefaultResolvedModuleId(null, null, null);
        ConfigureUtil.configureByMap(notation, out);
        return out;
    }

    private ModuleIdentifier parseString(Object notation) {
        String[] split = notation.toString().split(":");
        if (split.length != 3) {
            throw new InvalidDependencyFormat(
                "Invalid format: '" + notation + "'. Correct notation is a 3-part group:name:version notation,"
                + "e.g: org.gradle:gradle-core:1.0-milestone-3");
        }
        final String group = split[0];
        final String name = split[1];
        final String version = split[2];
        return identifier(group, name, version);
    }

    static ModuleIdentifier identifier(final String group, final String name, final String version) {
        return new DefaultResolvedModuleId(group, name, version);
    }

    public static class InvalidDependencyFormat extends RuntimeException {
        public InvalidDependencyFormat(String message) {
            super(message);
        }
    }
}