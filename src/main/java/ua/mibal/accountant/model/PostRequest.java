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
import static java.lang.String.format;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        this.dataPrinter = dataPrinter;
        String name = "";
        String data = "";
        while (name.equals("")) {
            dataPrinter.printInfoMessage("Enter name of new commit:");
            name = inputReader.read().trim();
        }
        while (data.equals("")) {
            dataPrinter.printInfoMessage("Enter data of new commit:");
            data = inputReader.read();
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        LocalDateTime now = LocalDateTime.now();
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
                account.getPath(), account.getName()
            ));
            e.printStackTrace();
        }
    }
}
