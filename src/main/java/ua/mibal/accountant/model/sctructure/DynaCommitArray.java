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

package ua.mibal.accountant.model.sctructure;

import ua.mibal.accountant.model.Commit;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class DynaCommitArray {
    Commit[] data;

    int count = 0;

    public DynaCommitArray() {
        data = new Commit[5];
    }

    public void add(Commit commit) {
        if (count >= data.length) {
            Commit[] newData = new Commit[data.length * 2];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
        data[count] = commit;
        count++;
    }

    public Commit[] asArray() {
        Commit[] result = new Commit[count];
        System.arraycopy(data, 0, result, 0, count);
        return result;
    }
}
