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

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Accountant {

    Account[] accounts;

    final DataPrinter dataPrinter;

    final InputReader inputReader;

    final DataOperator dataOperator;

    public Accountant(final DataPrinter dataPrinter, final InputReader inputReader, final DataOperator dataOperator) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
        this.dataOperator = dataOperator;

        accounts = getAccountsFromList();
    }

    private Account[] getAccountsFromList() {
        return dataOperator.getAccountsFromList();
    }

    public Account getCurrentAccount() {
        Account desiredAccount = null;
        if (accounts == null || accounts.length == 0) {
            dataPrinter.printInfoMessage("""
                    There are no accounts. You can create new.
                    Enter name of a new account:""");
            String name;
            while (true) {
                name = inputReader.read().trim();
                if (name.length() < 3) {
                    dataPrinter.printInfoMessage("""
                            Name must contain 3 and more characters.
                            Enter name:""");
                } else {
                    break;
                }
            }
            desiredAccount = new Account(name, dataOperator);
            addAccountToAccountList(desiredAccount);
        } else {
            boolean isFind = false;
            String name = "";
            while (!isFind) {
                if(name.equals("")){
                    dataPrinter.printInfoMessage("Enter name of Account");
                    name = inputReader.read().trim();
                }
                for (final Account account : accounts) {
                    if (name.equals(account.getName())) {
                        desiredAccount = account;
                        isFind = true;
                        break;
                    }
                }
                name = "";
                if (!isFind) {
                    dataPrinter.printInfoMessage("There are no account with this name. I have the next accounts: " + '\n');
                    for (final Account account : accounts) {
                        dataPrinter.printInfoMessage(account.getName());
                    }
                    dataPrinter.printInfoMessage('\n' + "If you need to add or create new account, enter '/add'.");
                    String command = inputReader.read().trim();
                    if (command.charAt(0) == '/') {
                        if (command.equalsIgnoreCase("/add")) {
                            desiredAccount = addNewAccount();
                            addAccountToAccountList(desiredAccount);
                            isFind = true;
                        }
                    } else {
                        name = command;
                    }
                }
            }
        }
        return desiredAccount;
    }

    private Account addNewAccount() {
        String name;
        while (true) {
            dataPrinter.printInfoMessage("Enter name of account:");
            name = inputReader.read().trim();
            if (name.length() < 3) {
                dataPrinter.printInfoMessage("Name must contain 3 and more characters.");
            } else {
                break;
            }
        }
        return new Account(name, dataOperator);
    }

    private void addAccountToAccountList(final Account account) {
        if(!dataOperator.addAccountToAccountList(account)){
            dataPrinter.printErrorMessage("Error with adding account to accountList");
        }
    }
}
