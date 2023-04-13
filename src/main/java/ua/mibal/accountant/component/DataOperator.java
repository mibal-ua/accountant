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
import java.util.List;

/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public interface DataOperator {

    List<Commit> getCommits(final Account account);

    void addCommit(final Account account, final Commit commitToAdd);

    void createAccountFile(Account account);

    String getPathToAccount(Account account);

    boolean isAccountExist(Account account);

    List<Account> getAllAccounts();

    boolean addAccountToAccountList(Account account);
}
