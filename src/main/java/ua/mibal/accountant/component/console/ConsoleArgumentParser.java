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

package ua.mibal.accountant.component.console;

import ua.mibal.accountant.component.DataPrinter;
import ua.mibal.accountant.component.InputReader;
import ua.mibal.accountant.component.config.ArgumentParser;
import ua.mibal.accountant.component.config.Arguments;
import ua.mibal.accountant.model.ProgramMode;

import static ua.mibal.accountant.model.ProgramMode.ADD;
import static ua.mibal.accountant.model.ProgramMode.GET;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class ConsoleArgumentParser implements ArgumentParser {

    final DataPrinter dataPrinter;

    final InputReader inputReader;

    public ConsoleArgumentParser(final DataPrinter dataPrinter, final InputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
    }

    @Override
    public Arguments parse(String[] args) {
        ProgramMode programMode;
        while (true) {
            dataPrinter.printInfoMessage("Enter program mode (get/add):");
            String str = inputReader.read();
            if (ADD.name().equalsIgnoreCase(str) || GET.name().equalsIgnoreCase(str)) {
                programMode = ProgramMode.valueOf(str.toUpperCase());
                break;
            }
        }
        return new Arguments(programMode);
    }
}
