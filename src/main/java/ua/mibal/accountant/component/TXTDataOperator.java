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
import ua.mibal.accountant.model.Commit;
import ua.mibal.accountant.model.sctructure.DynaAccountArray;
import ua.mibal.accountant.model.sctructure.DynaCommitArray;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class TXTDataOperator implements DataOperator {

    @Override
    public Commit[] getCommits(final Account account) {

        File file = new File(account.getPath());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        DynaCommitArray commits = new DynaCommitArray();
        String line;
        String time = null;
        String name = null;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine(); //беремо строчку
            if (isNumber(line.charAt(0))) {
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < line.length(); j++) {
                    char ch = line.charAt(j);
                    if (ch != '|') {
                        str.append(ch);
                    } else {
                        time = str.toString().trim();
                        str = new StringBuilder();
                    }
                }
                name = str.toString().trim();
            } else {
                commits.add(new Commit(time, name, line));
            }
        }
        scanner.close();
        return commits.asArray();
    }

    private boolean isNumber(final char ch) {
        return '0' <= ch && ch <= '9';
    }

    @Override
    public void addCommit(final Account account, final Commit commitToAdd) {
        File file = new File(account.getPath());
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter out = new PrintWriter(bufferedWriter);

            out.print(commitToAdd);

            bufferedWriter.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAccountFile(final Account account) {
        File file = new File(account.getPath());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPathToAccount(final Account account) {
        return System.getProperty("user.home") + "/" + account.getName() + ".txt";
    }

    @Override
    public boolean isAccountExist(final Account account) {
        File file = new File(account.getPath());
        return file.exists();
    }

    @Override
    public Account[] getAccountsFromList() {
        File file = new File(System.getProperty("user.home") + "/accountsList.txt");
        if (file.exists()) {
            DynaAccountArray result = new DynaAccountArray();
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            StringBuilder newList = new StringBuilder();

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                if(!name.equals("")){
                    File accountFile = new File(System.getProperty("user.home") + "/" + name + ".txt");
                    if (accountFile.exists()) {
                        result.add(new Account(name, this));
                        newList.append(name).append('\n');
                    }
                }
            }
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file.getAbsolutePath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writer.println(newList);
            writer.close();
            return result.asArray();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean addAccountToAccountList(final Account account) {
        File file = new File(System.getProperty("user.home") + "/accountsList.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter out = new PrintWriter(bufferedWriter);

            out.print(account.getName() + '\n');

            bufferedWriter.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
