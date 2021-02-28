package net.abdulahad.suhasini.data;

public class SqlQuery {

    public static final String CREATE_TABLE_DEPOSIT = "CREATE TABLE " +
            Key.TABLE_DEPOSIT + " ( " +
            Key.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            Key.PREVIOUS + " REAL NOT NULL DEFAULT 0, " +
            Key.CURRENT + " REAL NOT NULL, " +
            Key.TAG + " TEXT NOT NULL, " +
            Key.CALCULATED + " INTEGER NOT NULL DEFAULT 0, " +
            Key.TRANSACTION_TYPE + " INTEGER NOT NULL, " +
            Key.HAPPENED + " TEXT NOT NULL, " +
            Key.SYNC + " INTEGER NOT NULL DEFAULT 0)";

    public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " +
            Key.TABLE_TRANSACTION + " ( " +
            Key.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            Key.AMOUNT + " REAL NOT NULL, " +
            Key.TAG + " TEXT NOT NULL, " +
            Key.TYPE + " INTEGER NOT NULL DEFAULT 0, " +
            Key.ACCOUNTED + " INTEGER NOT NULL DEFAULT 0, " +
            Key.HAPPENED + " TEXT NOT NULL, " +
            Key.SYNC + " INTEGER NOT NULL DEFAULT 0)";

    public static final String CREATE_TABLE_ERROR = "CREATE TABLE " +
            Key.TABLE_ERROR + " ( " +
            Key.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            Key.ERROR_HOUSE + " TEXT NOT NULL, " +
            Key.ERROR_NAME + " TEXT NOT NULL, " +
            Key.ERROR_MSG + " TEXT NOT NULL)";

    public static final String QUERY_TRANS_HAPPENED_ON = "SELECT DISTINCT date(trans.happened) AS DAY FROM trans WHERE strftime('%Y-%m', trans.happened) = ? ORDER BY DAY DESC";

    public static final String QUERY_TRANS_ON_SPEC_DATE = "SELECT * FROM trans WHERE date(trans.happened) = ? ORDER BY trans.id DESC";

    public static final String QUERY_POCKET_HISTORY = "SELECT * FROM deposit WHERE strftime('%Y-%m', deposit.happened) = ? ORDER BY deposit.id DESC";

    public static final String QUERY_SPENDING_BY_CATEGORY = "SELECT trans.type, SUM(trans.amount) as total FROM trans WHERE strftime('%Y-%m', trans.happened) = ? GROUP BY trans.type ORDER BY total DESC";

    public static final String QUERY_CURRENT_DEPOSIT = "SELECT current FROM deposit WHERE id=(SELECT MAX(id) FROM deposit)";

    public static final String QUERY_EARNING_BY_MONTH = " SELECT sum(deposit.current - deposit.previous) as total FROM deposit WHERE deposit.trans_type = 0 AND strftime('%Y-%m', deposit.happened) = ?";

    public static String getSyncCountQuery(String table) {
        return String.format("SELECT count(%s.sync) FROM %s WHERE %s.sync = 0", table, table, table);
    }

}
