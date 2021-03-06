/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.api.internal.tasks.compile;

import org.gradle.api.internal.file.TemporaryFileProvider;
import org.gradle.api.tasks.WorkResult;
import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Executes the Java command line compiler specified in {@code JavaCompileSpec.forkOptions.getExecutable()}.
 */
public class CommandLineJavaCompiler implements Compiler<JavaCompileSpec> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineJavaCompiler.class);

    private final CommandLineJavaCompilerArgumentsGenerator argumentsGenerator;
    private final File workingDir;

    public CommandLineJavaCompiler(TemporaryFileProvider tempFileProvider, File workingDir) {
        argumentsGenerator = new CommandLineJavaCompilerArgumentsGenerator(tempFileProvider);
        this.workingDir = workingDir;
    }

    public WorkResult execute(JavaCompileSpec spec) {
        String executable = spec.getCompileOptions().getForkOptions().getExecutable();
        LOGGER.info("Compiling with Java command line compiler '{}'.", executable);

        Iterable<String> args = argumentsGenerator.generate(spec);
        ExecHandle handle = createCompilerHandle(executable, args);
        executeCompiler(handle);

        return new SimpleWorkResult(true);
    }

    private ExecHandle createCompilerHandle(String executable, Iterable<String> args) {
        ExecHandleBuilder builder = new ExecHandleBuilder();
        builder.setWorkingDir(workingDir);
        builder.setExecutable(executable);
        builder.setArgs(args);
        builder.setIgnoreExitValue(true);
        return builder.build();
    }

    private void executeCompiler(ExecHandle handle) {
        handle.start();
        ExecResult result = handle.waitForFinish();
        if (result.getExitValue() != 0) {
            throw new CompilationFailedException(result.getExitValue());
        }
    }
}
