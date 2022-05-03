package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String COL_ACCOUNT_NO="accountNo";
    private static final String COL_BANK_NAME="bankName";
    private static final String COL_ACCOUNT_HOLDER_NAME="accountHolderName";
    private static final String COL_BALANCE= "balance";
    private static final String ACCOUNT_TABLE="accountDetails";

    private  static final String COL_TRANSACTION_ID="transactionId";
    private  static final String COL_date="date";
    //private  static final String COL_ACCOUNT_NO="accountNo";
    private  static final String COL_EXPENSE_TYPE="expenseType";
    private  static final String COL_AMOUNT="amount";
    private  static final String TRANSACTION_TABLE="transactionDetails";


    public SQLiteHelper(Context context) {
        super(context, "190058R.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
        String create_account_table_query="CREATE TABLE "+ACCOUNT_TABLE+" ("+COL_ACCOUNT_NO+" VARCHAR(20) PRIMARY KEY NOT NULL, "+COL_BANK_NAME +" VARCHAR(20) NOT NULL, "+ COL_ACCOUNT_HOLDER_NAME+" VARCHAR(30) NOT NULL, "+COL_BALANCE+" REAL NOT NULL)";
        String create_transaction_table_query="CREATE TABLE "+TRANSACTION_TABLE+" ("+COL_TRANSACTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_ACCOUNT_NO +" VARCHAR(20) NOT NULL, "+ COL_EXPENSE_TYPE+" VARCHAR(10) NOT NULL, "+COL_AMOUNT+" REAL NOT NULL, "+COL_date+" TEXT NOT NULL,FOREIGN KEY("+COL_ACCOUNT_NO+") REFERENCES "+ACCOUNT_TABLE+"("+COL_ACCOUNT_NO+"))";
        sqLiteDatabase.execSQL(create_account_table_query);
        sqLiteDatabase.execSQL(create_transaction_table_query);
        }
        catch (Exception e){
            Log.d("error", "setup:"+e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ACCOUNT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TRANSACTION_TABLE);
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_date,transaction.getDate().toString());
        cv.put(COL_ACCOUNT_NO,transaction.getAccountNo());
        cv.put(COL_EXPENSE_TYPE,transaction.getExpenseType().toString());
        cv.put(COL_AMOUNT,transaction.getAmount());
        db.insert(TRANSACTION_TABLE,null,cv);
        db.close();
    }


    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do{
                Date date=new Date(cursor.getString(4));
                String accountNo=cursor.getString(1);
                ExpenseType expenseType=ExpenseType.values()[cursor.getString(2).equals("EXPENSE")?0:1];
                double amount=cursor.getDouble(3);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;

    }


    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions=getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();
        String query = "SELECT " + COL_ACCOUNT_NO+" FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do{
                String accountNo=cursor.getString(0);
                accountNumberList.add(accountNo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountNumberList;

    }


    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        String query = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do{
                String accountNo=cursor.getString(0);
                String bankName=cursor.getString(1);
                String accountHolderName=cursor.getString(2);
                double balance=cursor.getDouble(3);

                Account account = new Account(accountNo,bankName ,accountHolderName,balance);
                accountList.add(account);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountList;

    }


    public Account getAccount(String accountNo) throws InvalidAccountException {
        String query="SELECT * FROM "+ACCOUNT_TABLE+" WHERE "+COL_ACCOUNT_NO+"='"+accountNo+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Account account=null;
        if (cursor.moveToFirst()) {
            String bankName=cursor.getString(1);
            String accountHolderName=cursor.getString(2);
            double balance=cursor.getDouble(3);
            account = new Account(accountNo,bankName ,accountHolderName,balance);
        }
        cursor.close();
        db.close();
        return account;
    }


    public void addAccount(Account account) {
        String accountNo=account.getAccountNo();
        String bankName=account.getBankName();
        String accountHolderName=account.getAccountHolderName();
        double balance=account.getBalance();

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_ACCOUNT_NO,accountNo);
        cv.put(COL_BANK_NAME,bankName);
        cv.put(COL_ACCOUNT_HOLDER_NAME,accountHolderName);
        cv.put(COL_BALANCE,balance);
        db.insertWithOnConflict(ACCOUNT_TABLE,null,cv,SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

    }


    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=getReadableDatabase();
        db.delete(ACCOUNT_TABLE,COL_ACCOUNT_NO +"='"+accountNo+"'",null);
        db.close();
    }


    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account=getAccount(accountNo);
        SQLiteDatabase db=getReadableDatabase();
        switch (expenseType){
            case EXPENSE:
                account.setBalance(account.getBalance()-amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance()+amount);
                break;
        }
        ContentValues cv=new ContentValues();
        cv.put(COL_BALANCE,account.getBalance());
        db.update(ACCOUNT_TABLE,cv,COL_ACCOUNT_NO+"='"+accountNo+"'",null);
        db.close();
    }
}
