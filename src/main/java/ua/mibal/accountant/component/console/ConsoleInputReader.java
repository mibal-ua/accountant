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

import ua.mibal.accountant.component.InputReader;
import java.util.Scanner;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class ConsoleInputReader implements InputReader {

    private final ExitHandler exitHandler;

    public ConsoleInputReader(final ExitHandler exitHandler) {
        this.exitHandler = exitHandler;
    }

    @Override
    public String read() {
        final String str = new Scanner(System.in).nextLine().trim();
        if (str.equalsIgnoreCase("/exit")) {
            exitHandler.exit();
        }
        return str;
    }
}
