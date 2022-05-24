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

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class Commit {

    private final String content;

    private final String time;

    private final String name;

    public Commit(final String time, final String name, final String data) {
        this.time = time;
        this.name = name;
        this.content = data;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return time + " | " + name + '\n' +
               content + '\n';
    }
}
