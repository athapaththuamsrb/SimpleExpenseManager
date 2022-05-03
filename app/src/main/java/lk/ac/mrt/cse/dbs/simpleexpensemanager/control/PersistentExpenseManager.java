package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private final Context context;
    public PersistentExpenseManager(Context context) {
        this.context=context;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            Log.d("error", "setup:"+e.getMessage());
        }

    }

    @Override
    public void setup() throws ExpenseManagerException {
        /*** Begin generating dummy data for In-Memory implementation ***/
    try {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(sqLiteHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(sqLiteHelper);
        setAccountsDAO(persistentAccountDAO);


    // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        persistentAccountDAO.addAccount(dummyAcct1);
        persistentAccountDAO.addAccount(dummyAcct2);

    /*** End ***/
    }
    catch(Exception e){
        Log.d("error", "setup:"+e.getMessage());
    }
    }
}
