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

import ua.mibal.accountant.component.console.ConsoleDataPrinter;
import ua.mibal.accountant.component.console.ConsoleInputReader;
import ua.mibal.accountant.model.*;

import static java.lang.String.format;
import static ua.mibal.accountant.model.RequestMode.ADD;
import static ua.mibal.accountant.model.RequestMode.GET;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class RequestMaker {

    final DataPrinter dataPrinter = new ConsoleDataPrinter();

    final InputReader inputReader = new ConsoleInputReader();

    final DataParser dataParser = new TXTDataParser();

    final Accountant accountant = new Accountant(dataPrinter, inputReader, dataParser);

    final String[] args;

    public RequestMaker(final String[] args) {
        this.args = args;
    }

    public void make() {
        dataPrinter.printInfoMessage("You can exit everywhere by the typing '/exit'.");
        Account account = accountant.getCurrentAccount();
        while (true) {
            RequestMode requestMode;
            if (account.isEmpty()) {
                requestMode = ADD;
            } else {
                while (true) {
                    dataPrinter.printInfoMessage("Enter what you want: '/get' or '/add'");
                    String str = inputReader.read();
                    if (!str.equals("")) {
                        if (str.charAt(0) == '/') {
                            str = str.substring(1);
                            if (ADD.name().equalsIgnoreCase(str) || GET.name().equalsIgnoreCase(str)) {
                                requestMode = RequestMode.valueOf(str.toUpperCase());
                                break;
                            }
                            if (str.equalsIgnoreCase("other")) {
                                account = accountant.getCurrentAccount();
                                if (account.isEmpty()) {
                                    requestMode = ADD;
                                    break;
                                }
                            }
                        } else {
                            dataPrinter.printInfoMessage(format(
                                    "String '%s' is not command, you must start with '/' char.", str));
                        }
                    }
                }
            }
            Request request = switch (requestMode){
                case GET -> new GetRequest(inputReader, dataPrinter);
                case ADD -> new PostRequest(inputReader, dataPrinter);
            };
            request.make(account);
            dataPrinter.printInfoMessage("For the choice the another account, enter '/other'.");
        }
    }
}
