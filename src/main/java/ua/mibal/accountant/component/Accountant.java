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

import ua.mibal.accountant.model.Account;
import static java.lang.String.format;
import static ua.mibal.accountant.model.Account.MIN_ACCOUNT_NAME_LENGTH;
import java.util.List;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Accountant {

    final DataPrinter dataPrinter;

    final InputReader inputReader;

    final DataOperator dataOperator;

    List<Account> accounts;

    public Accountant(final DataPrinter dataPrinter, final InputReader inputReader, final DataOperator dataOperator) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
        this.dataOperator = dataOperator;

        accounts = dataOperator.getAllAccounts();
    }

    public Account selectAccount(final Account lastAccount) {
        if (lastAccount == null) {
            return selectAccount();
        }
        String variant;
        dataPrinter.printInfoMessage("""
            Enter:
            1 - Work with current account
            2 - Select another""");
        while (true) {
            variant = inputReader.read().trim();
            if (variant.equals("1")) {
                dataPrinter.clearLines(3);
                return lastAccount;
            } else if (variant.equals("2")) {
                dataPrinter.clearLines(3);
                return selectAccount();
            }
            dataPrinter.clearLines(1);
        }
    }

    private Account selectAccount() {
        int clearingCount = 6;
        if (accounts.size() == 0) {
            createNewAccount();
            clearingCount = 7;
        }
        return findAccount(clearingCount);
    }

    private Account findAccount(final int clearingCount) {
        int count = clearingCount;
        while (true) {
            dataPrinter.printInfoMessage("List of accounts:");
            dataPrinter.printInfoMessage("");
            for (int i = 0; i < accounts.size(); i++) {
                dataPrinter.printInfoMessage(
                    format("%d. %s", i + 1, accounts.get(i).getName()));
            }
            dataPrinter.printInfoMessage("");
            final String input;
            dataPrinter.printInfoMessage("If you need to create/delete new account, enter '/add' or '/delete'.");
            dataPrinter.printInfoMessage("Enter name/index of account");
            input = inputReader.read().trim();
            dataPrinter.clearLines(count + accounts.size());
            if (input.charAt(0) == '/') {
                if (input.equalsIgnoreCase("/add")) {
                    createNewAccount();
                }
                if (input.equalsIgnoreCase("/delete")) {
                    deleteAccount();
                }
                count = 7;
            } else {
                if (input.length() > 1) {
                    for (final Account account : accounts) {
                        if (input.equals(account.getName())) {
                            return account;
                        }
                    }
                    dataPrinter.printInfoMessage(format(
                        "There are no accounts with name '%s'.", input));
                    count = 7;
                } else {
                    if (Character.isDigit(input.charAt(0))) {
                        final int inputIndex = Integer.parseInt(input);
                        if (1 <= inputIndex && inputIndex <= accounts.size()) {
                            return accounts.get(inputIndex - 1);
                        } else {
                            dataPrinter.printInfoMessage(format(
                                "Account with index '%s' does not exists in list.", inputIndex));
                            count = 7;
                        }
                    }
                }
            }
        }
    }

    private void deleteAccount() {
        Account accountToDelete;
        String name;
        dataPrinter.printInfoMessage("Enter name of account you want to delete:");
        while (true) {
            name = inputReader.read().trim();
            dataPrinter.clearLines(2);
            for (final Account account : accounts) {
                if (name.equals(account.getName())) {
                    accountToDelete = account;
                    if (dataOperator.deleteAccount(accountToDelete)) {
                        accounts = dataOperator.getAllAccounts();
                        dataPrinter.printInfoMessage(format("Account '%s' successfully deleted!", name));
                        return;
                    } else {
                        dataPrinter.printErrorMessage("Error with deleting account");
                    }
                }
            }
            dataPrinter.printInfoMessage(format(
                "There are no accounts with name '%s'.", name));
        }
    }

    private void createNewAccount() {
        String name;
        dataPrinter.printInfoMessage("Enter name of a new account:");
        while (true) {
            name = inputReader.read().trim();
            dataPrinter.clearLines(2);
            if (name.length() < MIN_ACCOUNT_NAME_LENGTH) {
                dataPrinter.printInfoMessage(format(
                    "Incorrect name '%s', must contain %d and more characters.",
                    name, MIN_ACCOUNT_NAME_LENGTH
                ));
            } else {
                break;
            }
        }
        final Account account = new Account(name, dataOperator);
        if (dataOperator.addAccountToAccountList(account)) {
            accounts = dataOperator.getAllAccounts();
            dataPrinter.printInfoMessage(format("Account '%s' successfully added!", name));
        } else {
            dataPrinter.printErrorMessage("Error with adding account to accountList");
        }
    }
}
