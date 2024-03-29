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

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class ConsoleDataPrinter implements DataPrinter {

    private static String GO_TO_PREVIOUS_LINE_ESC = "\033[F";

    private static String CLEAR_CURRENT_LINE_ESC = "\033[2K";

    @Override
    public void printInfoMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void printErrorMessage(final String message) {
        System.err.println(message);
    }

    @Override
    public void clearLines(final int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(GO_TO_PREVIOUS_LINE_ESC);
            System.out.print(CLEAR_CURRENT_LINE_ESC);
        }
    }
}
