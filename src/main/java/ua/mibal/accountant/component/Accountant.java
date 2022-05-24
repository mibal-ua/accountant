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

package ua.mibal.accountant.component;

import ua.mibal.accountant.component.config.Arguments;
import ua.mibal.accountant.component.config.CommandLineArgumentParser;
import ua.mibal.accountant.component.console.ConsoleArgumentParser;
import ua.mibal.accountant.component.console.ConsoleDataPrinter;
import ua.mibal.accountant.component.console.ConsoleInputReader;
import ua.mibal.accountant.model.GetRequest;
import ua.mibal.accountant.model.PostRequest;
import ua.mibal.accountant.model.ProgramMode;
import ua.mibal.accountant.model.Request;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.String.format;
import static ua.mibal.accountant.model.ProgramMode.GET;
import static ua.mibal.accountant.model.ProgramMode.ADD;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Accountant {

    final ProgramMode programMode;

    final DataPrinter dataPrinter = new ConsoleDataPrinter();

    final InputReader inputReader = new ConsoleInputReader();

    public Accountant(final String[] args) {
        final Arguments arguments = new ConsoleArgumentParser(dataPrinter, inputReader).parse();
        this.programMode = arguments.getProgramMode();
    }

    public void create() {
        Request request;
        if (programMode == GET) {
            request = new GetRequest(inputReader, dataPrinter);
        } else if (programMode == ADD) {
            request = new PostRequest(inputReader, dataPrinter);
        } else {
            throw new IllegalArgumentException(format(
                    "Program mode have illegal argument '%s'.", programMode.name()));
        }
        //TODO many accounts
        Account account = new Account("name", dataPrinter);
        request.make(account);

        System.out.println("Press any key to continue...");
        new Scanner(System.in).nextLine();
    }
}
