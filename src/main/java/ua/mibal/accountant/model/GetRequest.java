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

import static java.lang.String.format;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class GetRequest implements Request {

    private String name;

    private String time;

    private String content;

    private final DataPrinter dataPrinter;

    private final InputReader inputReader;

    public GetRequest(final InputReader inputReader, final DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
        constructAllFields();
    }

    private void constructAllFields() {
        StringBuilder userRequest = new StringBuilder();
        while (userRequest.toString().equals("")) {
            dataPrinter.printInfoMessage("Enter time, name or content of commit:");
            userRequest = new StringBuilder().append(inputReader.read());
        }

        if (userRequest.toString().equalsIgnoreCase("all")) {
            this.name = null;
            this.time = null;
            this.content = null;
        } else if (!(userRequest.length() < 3) && (userRequest.charAt(1) == '.' || userRequest.charAt(2) == '.')) {
            if (userRequest.charAt(1) == '.') {
                userRequest.insert(0, 0);
            }
            if ((userRequest.length() == 4)) {
                userRequest.insert(3, 0);
            }
            this.time = userRequest.toString();
            this.name = null;
            this.content = null;
        } else {
            this.time = null;
            String str;
            while (true) {
                dataPrinter.printInfoMessage("Is this a Name? (Y/N)");
                str = inputReader.read().trim();
                if (str.length() == 1) {
                    if (str.equalsIgnoreCase("Y")) {
                        this.name = userRequest.toString();
                        this.content = null;
                        break;
                    }
                    if (str.equalsIgnoreCase("N")) {
                        this.content = userRequest.toString();
                        this.name = null;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void make(final Account account) {
        Commit[] commits;
        try {
            commits = account.getCommits();
        } catch (IOException e) {
            dataPrinter.printErrorMessage(format(
                    "Account file '%s' does not exists or renamed. Current name: '%s'",
                    account.getPATH(), account.getName()
            ));
            e.printStackTrace();
            return;
        }
        String result;
        if (name != null) {
            result = findByName(commits, name);
        } else if (time != null) {
            result = findByTime(commits, time);
        } else if (content != null) {
            result = findByContent(commits, content);
        } else {
            result = findAll(commits);
        }

        if (result.equals("")) {
            dataPrinter.printInfoMessage("Data by current request is empty.");
        } else {
            dataPrinter.printInfoMessage("Commits by request:");
            dataPrinter.printInfoMessage(result);
        }

    }

    private String findByContent(final Commit[] commits, final String content) {
        return findByString(commits, content, Commit::getContent);
    }

    private String findAll(final Commit[] commits) {
        StringBuilder str = new StringBuilder();
        for (final Commit commit : commits) {
            str.append(commit.toString());
        }
        return str.toString();
    }

    private String findByTime(final Commit[] commits, final String time) {
        StringBuilder result = new StringBuilder();
        for (final Commit commit : commits) {
            String commitTime = commit.getTime();
            for (int i = 0; i < time.length(); i++) {
                if (time.charAt(i) != commitTime.charAt(i)) {
                    break;
                } else if (time.length() - 1 == i) {
                    result.append(commit);
                }
            }
        }
        return result.toString();
    }

    private String findByName(final Commit[] commits, final String name) {
        return findByString(commits, name, Commit::getName);
    }

    private String findByString(final Commit[] commits, final String request, final Lambda lambda) {
        StringBuilder result = new StringBuilder();
        String[] requestKeyWords = request.split(" ");
        String[] commitKeyWords;
        for (final Commit commit : commits) {
            commitKeyWords = lambda.getValue(commit).trim().split(" ");//
            for (final String commitWord : commitKeyWords) {
                for (final String requestKeyWord : requestKeyWords) {
                    int count = 0;
                    if (commitWord.equalsIgnoreCase(requestKeyWord)) {
                        result.append(commit);
                        break;
                    }
                    int minLengthOfWords = Math.min(requestKeyWord.length(), commitWord.length());
                    for (int i = 0; i < minLengthOfWords; i++) {
                        if (commitWord.charAt(i) == requestKeyWord.charAt(i)) {
                            count++;
                        }
                        if (count == 3) {
                            result.append(commit);
                            break;
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * @author Michael Balakhon
     * @link http://t.me/mibal_ua
     */
    @FunctionalInterface
    interface Lambda {

        String getValue(Commit commit);
    }
}