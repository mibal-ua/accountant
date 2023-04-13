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

import ua.mibal.accountant.model.GetRequest;
import ua.mibal.accountant.model.PostRequest;
import ua.mibal.accountant.model.Request;
import ua.mibal.accountant.model.RequestMode;
import static java.lang.String.format;
import static ua.mibal.accountant.model.RequestMode.ADD;
import static ua.mibal.accountant.model.RequestMode.GET;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class RequestMaker {

    private final DataPrinter dataPrinter;

    private final InputReader inputReader;

    public RequestMaker(final DataPrinter dataPrinter, final InputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
    }

    public Request make() {
        RequestMode requestMode;
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
                } else {
                    dataPrinter.printInfoMessage(format(
                        "String '%s' is not command, you must start with '/' char.", str));
                }
            }
        }
        return make(requestMode);
    }

    public Request make(final RequestMode requestMode) {
        return switch (requestMode) {
            case GET -> new GetRequest(inputReader, dataPrinter);
            case ADD -> new PostRequest(inputReader, dataPrinter);
        };
    }
}
