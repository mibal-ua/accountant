/*
 * Copyright (c) 2022. http://t.me/mibal_ua
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

package ua.mibal.accountant.component.config;

import ua.mibal.accountant.model.ProgramMode;

import static ua.mibal.accountant.model.ProgramMode.GET;
import static ua.mibal.accountant.model.ProgramMode.POST;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class CommandLineArgumentParser {

    private final String[] args;

    public CommandLineArgumentParser(final String[] args) {
        this.args = args;
    }

    public CommandLineArguments parse() {
        ProgramMode programMode = null;
        for (final String arg : args) {
            if (POST.name().equalsIgnoreCase(arg) || GET.name().equalsIgnoreCase(arg)) {
                if (programMode == null) {
                    programMode = ProgramMode.valueOf(arg.toUpperCase());
                } else {
                    System.err.printf("Invalid command line argument: '%s', because level already set: '%s'.%n",
                            arg, programMode
                    );
                }
            }
        }
        if (programMode == null) {
            programMode = GET;
        }
        return new CommandLineArguments(programMode);
    }

    public static class CommandLineArguments {
        ProgramMode programMode;

        public ProgramMode getProgramMode() {
            return programMode;
        }

        public CommandLineArguments(final ProgramMode programMode) {
            this.programMode = programMode;
        }
    }
}
