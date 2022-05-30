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

package ua.mibal.accountant.model;

import ua.mibal.accountant.component.DataPrinter;
import ua.mibal.accountant.component.InputReader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class PostRequest implements Request {

    private final DataPrinter dataPrinter;

    private final String name;

    private final String time;

    private final String data;

    public PostRequest(final InputReader inputReader, final DataPrinter dataPrinter) {
        String name = "";
        String data = "";
        while (name.equals("")) {
            dataPrinter.printInfoMessage("Enter name of new commit:");
            name = inputReader.read().trim();
        }
        while (data.equals("")) {
            dataPrinter.printInfoMessage("Enter data of new commit:");
            data = inputReader.read();
            /*
            StringBuilder result = new StringBuilder();
            StringBuilder str;
            while (true) {
                System.out.println(1);
                str = new StringBuilder().append(inputReader.read());
                if (str.length() != 0) {
                    if (isNumber(str.charAt(0))) {
                        dataPrinter.printInfoMessage("Line mustn't contain number '" + str.charAt(0) + "' at the start of line.");
                    } else if (str.charAt(str.length() - 1) == '\\') {
                        result.append(str.deleteCharAt(str.length() - 1)).append('\n');
                    } else {
                        result.append(str).append('\n');
                        break;
                    }
                } else {
                    dataPrinter.printInfoMessage("Line must contain any symbol.");
                }
            }
            data = result.toString();

             */

        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        LocalDateTime now = LocalDateTime.now();

        this.dataPrinter = dataPrinter;
        this.time = dtf.format(now);
        this.name = name;
        this.data = data;
    }

    @Override
    public void make(final Account account) {
        try {
            account.add(new Commit(time, name, data));
            dataPrinter.printInfoMessage("Commit successfully added.");
        } catch (IOException e) {
            dataPrinter.printInfoMessage("Commit doesn't added.");
            dataPrinter.printErrorMessage(format(
                    "Account file '%s' does not exists or renamed. Current name:%s",
                    account.getPATH(), account.getName()
            ));
            e.printStackTrace();
        }
    }

    private boolean isNumber(final char ch) {
        return ('1' <= ch && ch <= '9');
    }
}
