package net.abdulahad.suhasini.data;

public class Key {

    // 09/02/2021 01:05 AM
    public static final float DEFAULT_EXCHANGE_RATE = 115.102f;
    public static final float DEFAULT_ALLOWANCE = 100f;

    public static final String LOGGED_IN = "logged_id";

    public static final String LOCAL_DB_VERSION = "local_db_version";

    public static final String KEY_HAS_LOCK = "has_lock";
    public static final String KEY_LOCK = "lock_key";
    public static final String KEY_EXCHANGE_RATE = "exchange_rate";
    public static final String KEY_MONEY_SIGN = "money_sign";
    public static final String KEY_ALLOWANCE = "allowance";

    public static final String MONEY_SIGN_GBP = "£";
    public static final String MONEY_SIGN_BDT = "৳";

    public static final String URL_CURRENCY_API = "https://v6.exchangerate-api.com/v6/f9b0bf6e3ba39b1af8db890d/latest/GBP";

    public static final String URL_ONLINE = "https://abdulahad.net/pocket/";
    private static final String URL_DEBUG = "http://192.168.1.237/pocket/";

    public static final String URL_BASE = URL_ONLINE;
    public static final String URL_ACCOUNT_CHECK = URL_BASE + "account_check.php";

    public static final String URL_SYNC_DOWN = URL_BASE + "sync_down.php";
    public static final String URL_SYNC_PUSH = URL_BASE + "sync_push.php";

    public static final String TABLE_DEPOSIT = "deposit";
    public static final String TABLE_TRANSACTION = "trans";
    public static final String TABLE_ERROR = "error";

    public static final String ID = "id";
    public static final String PREVIOUS = "previous";
    public static final String CURRENT = "current";
    public static final String TAG = "tag";
    public static final String TRANSACTION_TYPE = "trans_type";
    public static final String CALCULATED = "calculated";
    public static final String HAPPENED = "happened";

    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String ACCOUNTED = "accounted";
    public static final String SYNC = "sync";

    public static final String ERROR_HOUSE = "error_house";
    public static final String ERROR_NAME = "error_name";
    public static final String ERROR_MSG = "error_msg";

    /* keys that come with server response */
    public static final String SERVER_KEY_CODE = "code";
    public static final String SERVER_KEY_MESSAGE = "msg";
    public static final String SERVER_KEY_VERSION = "version";
    public static final String SERVER_KEY_DATA = "data";

}
