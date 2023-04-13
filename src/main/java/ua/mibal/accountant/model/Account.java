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

import ua.mibal.accountant.component.DataOperator;

import java.io.IOException;
import java.util.List;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Account {

    private final String name;

    private static String PATH;

    private final DataOperator dataOperator;

    private List<Commit> lastCommits;

    private boolean emptiness = false;

    public Account(final String name, final DataOperator dataOperator) {
        this.dataOperator = dataOperator;
        this.name = name;
        PATH = dataOperator.getPathToAccount(this);
        if (!dataOperator.isAccountExist(this)) {
            dataOperator.createAccountFile(this);
            emptiness = true;
        }
        lastCommits = dataOperator.getCommits(this);
        if(lastCommits.size() == 0){
            emptiness = true;
        }
    }

    public final String getPath() {
        return PATH;
    }

    public final String getName() {
        return name;
    }

    public void add(final Commit commitToAdd) throws IOException {
        dataOperator.addCommit(this, commitToAdd);
        lastCommits = null;
        emptiness = false;
    }

    public List<Commit> getCommits() throws IOException {
        if (lastCommits == null) {
            lastCommits = dataOperator.getCommits(this);

        }
        return lastCommits;
    }

    public boolean isEmpty(){
        return emptiness;
    }
}
