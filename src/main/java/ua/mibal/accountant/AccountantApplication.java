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

package ua.mibal.accountant;

import ua.mibal.accountant.component.Accountant;
import ua.mibal.accountant.component.DataOperator;
import ua.mibal.accountant.component.DataPrinter;
import ua.mibal.accountant.component.InputReader;
import ua.mibal.accountant.component.RequestMaker;
import ua.mibal.accountant.component.TXTDataOperator;
import ua.mibal.accountant.component.console.ConsoleDataPrinter;
import ua.mibal.accountant.component.console.ConsoleInputReader;
import ua.mibal.accountant.model.Account;
import ua.mibal.accountant.model.Request;
import static ua.mibal.accountant.model.RequestMode.ADD;
import java.util.Scanner;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class AccountantApplication {

    private final DataPrinter dataPrinter = new ConsoleDataPrinter();

    private final InputReader inputReader = new ConsoleInputReader(() -> {
        System.out.println("Press any key to exit...");
        new Scanner(System.in).nextLine();
        System.exit(0);
    });

    private final DataOperator dataOperator = new TXTDataOperator();

    private final Accountant accountant = new Accountant(dataPrinter, inputReader, dataOperator);

    private final RequestMaker requestMaker = new RequestMaker(dataPrinter, inputReader);

    private Account lastAccount = null;

    public void start() {
        dataPrinter.printInfoMessage("You can exit everywhere by the typing '/exit'.");
        while (true) {
            final Account account = accountant.getCurrentAccount(lastAccount);
            Request request;
            if (account.isEmpty()) {
                request = requestMaker.make(ADD);
            } else {
                request = requestMaker.make();
            }
            request.make(account);
            lastAccount = account;
        }
    }
}
