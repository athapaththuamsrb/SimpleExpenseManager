package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO  implements AccountDAO {
    private SQLiteHelper sqLiteHelper;
    public PersistentAccountDAO(SQLiteHelper sqLiteHelper) {
        this.sqLiteHelper=sqLiteHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return sqLiteHelper.getAccountNumbersList();
    }

    @Override
    public List<Account> getAccountsList() {
        return sqLiteHelper.getAccountsList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return sqLiteHelper.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        sqLiteHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        sqLiteHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        sqLiteHelper.updateBalance(accountNo,expenseType,amount);
    }


}
