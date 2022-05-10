/*
 * Copyright 2022 http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ua.mibal.accountant;



import ua.mibal.accountant.component.Account;
import ua.mibal.accountant.component.DataPrinter;
import ua.mibal.accountant.component.FileParser;
import ua.mibal.accountant.component.InputReader;
import ua.mibal.accountant.console.ConsoleDataPrinter;
import ua.mibal.accountant.console.ConsoleInputReader;
import ua.mibal.accountant.model.Request;


import static java.lang.System.getProperty;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Launcher {

    static DataPrinter dataPrinter = new ConsoleDataPrinter();

    static FileParser fileParser = new FileParser();

    static InputReader inputReader = new ConsoleInputReader(dataPrinter);

    public static void main(String[] args) {
        Account account = new Account("name", fileParser);
        Request request = inputReader.read();
        dataPrinter.print(account.getData(request));
    }
}