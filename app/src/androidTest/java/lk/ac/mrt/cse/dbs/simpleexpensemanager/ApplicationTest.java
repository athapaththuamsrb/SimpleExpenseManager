/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;


import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest  {
    private static ExpenseManager expenseManager;

    @Before
    public void setExpenseManager() throws ExpenseManagerException {
        Context context= ApplicationProvider.getApplicationContext();
        expenseManager=new PersistentExpenseManager(context);
    }
    @Test
    public void  createTestAccount(){
        expenseManager.addAccount("35791R", "BOC", "Supun Athapathhthu", 800000.0);
        assertTrue(expenseManager.getAccountNumbersList().contains("35791R"));
    }

    @Test
    public void  addTestTransaction(){
        int numberOfTransaction=expenseManager.getTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("35791R", 9,5,2022, ExpenseType.EXPENSE,"2000");
            assertEquals(numberOfTransaction+1,expenseManager.getTransactionLogs().size());
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void  addTestTransactionExpense(){
        int numberOfTransaction=expenseManager.getTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("35791R", 9,5,2022, ExpenseType.EXPENSE,"2000");
            assertEquals(numberOfTransaction+1,expenseManager.getTransactionLogs().size());
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void  addTestTransactionIncome(){
        int numberOfTransaction=expenseManager.getTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("35791R", 9,5,2022, ExpenseType.INCOME,"2000");
            assertEquals(numberOfTransaction+1,expenseManager.getTransactionLogs().size());
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }
}