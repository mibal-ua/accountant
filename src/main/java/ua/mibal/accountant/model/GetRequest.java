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
import java.util.List;

import static java.lang.String.format;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class GetRequest implements Request {

    private String name;

    private String date;

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
            dataPrinter.printInfoMessage("""
                    Enter time, name or content of commit:
                    Or enter '/all' command""");
            userRequest = new StringBuilder().append(inputReader.read().trim());
        }
        if (userRequest.toString().equalsIgnoreCase("/all")) {
            this.name = null;
            this.date = null;
            this.content = null;
        } else if (isRequestByDate(userRequest.toString())) {
            if (userRequest.charAt(1) == '.') {
                userRequest.insert(0, '0');
            }
            if ((userRequest.length() == 4)) {
                userRequest.insert(3, '0');
            }
            this.date = userRequest.toString();
            this.name = null;
            this.content = null;
        } else {
            this.date = null;
            String str;
            dataPrinter.printInfoMessage("Is this a '/name' or a '/content'?");
            while (true) {
                str = inputReader.read().trim();
                if (str.charAt(0) == '/') {
                    String command = str.substring(1);
                    if(command.equalsIgnoreCase("name")){
                        this.name = userRequest.toString();
                        this.content = null;
                        break;
                    } else if(command.equalsIgnoreCase("content")){
                        this.name = null;
                        this.content = userRequest.toString();
                        break;
                    } else {
                        dataPrinter.printInfoMessage(
                                "String you enter is not command, you must choice among '/name' and '/content'."
                        );
                    }
                } else {
                    dataPrinter.printInfoMessage("String you enter is not command, you must start with '/' char.");
                }
            }
        }
    }

    private boolean isRequestByDate(final String userRequest) {
        return (!(userRequest.length() < 3) && (userRequest.charAt(1) == '.' || userRequest.charAt(2) == '.'));
    }

    @Override
    public void make(final Account account) {
        List<Commit> commits;
        try {
            commits = account.getCommits();
        } catch (IOException e) {
            dataPrinter.printErrorMessage(format(
                    "Account file '%s' does not exists or renamed. Current name: '%s'",
                    account.getPath(), account.getName()
            ));
            e.printStackTrace();
            return;
        }
        String result;
        if (name != null) {
            result = findByName(commits, name);
        } else if (date != null) {
            result = findByTime(commits, date);
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

    private String findByName(final List<Commit> commits, final String name) {
        return findByString(commits, name, Commit::getName);
    }

    private String findByContent(final List<Commit> commits, final String content) {
        return findByString(commits, content, Commit::getContent);
    }

    private String findByTime(final List<Commit> commits, final String time) {
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

    private String findAll(final List<Commit> commits) {
        StringBuilder str = new StringBuilder();
        for (final Commit commit : commits) {
            str.append(commit.toString());
        }
        return str.toString();
    }


    private String findByString(final List<Commit> commits, final String request, final Lambda lambda) {
        StringBuilder result = new StringBuilder();
        String[] requestKeyWords = request.split(" "); //розілили запит на слова в масиві
        String[] commitKeyWords;
        for (final Commit commit : commits) {
            commitKeyWords = lambda.getValue(commit).trim().split(" "); // узяли перший коміт, отримали данні, розділили
            for (final String commitWord : commitKeyWords) { //беремо одне слово з коміту
                for (final String requestKeyWord : requestKeyWords) { //беремо одне слово з запиту
                    if (commitWord.equalsIgnoreCase(requestKeyWord)) { //якщо повністю ідентичні - беремо коміт
                        result.append(commit);
                        break;
                    }
                    int minLengthOfWords = Math.min(requestKeyWord.length(), commitWord.length()); //рахуємо найменшу кількість букв в словах
                    int count = 0;
                    if (!(minLengthOfWords < 3)){ //якщо в нас є слово з 2 або 1 букв, ми його ігноруємо
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
