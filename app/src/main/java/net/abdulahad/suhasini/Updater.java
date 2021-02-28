package net.abdulahad.suhasini;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.model.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import kotlin.text.Charsets;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Updater {

    private final String[] tables = {Key.TABLE_DEPOSIT, Key.TABLE_TRANSACTION};
    private final Context appContext;
    SQLiteDatabase db;

    public Updater(Context appContext) {
        this.appContext = appContext;
        db = Suhasini.getDB(appContext);
    }

    public void updateBDTRate() {
        float rate = Key.DEFAULT_EXCHANGE_RATE;
        /* construct a GET request with required parameters */
        Request request = new Request.Builder().url(Key.URL_CURRENCY_API).build();
        OkHttpClient client = new OkHttpClient();
        try (Response serverResponse = client.newCall(request).execute()) {
            String data = serverResponse.body().string();
            JSONObject reply = new JSONObject(data);
            if (!reply.has("conversion_rates"))
                throw new Exception("Could not resolve the currency rate");
            rate = (float) reply.getJSONObject("conversion_rates").getDouble("BDT");
        } catch (Exception e) {
            ErrorRegister.register(e, Updater.class, db);
        }
        PrefHelper.putFloat(appContext, Key.KEY_EXCHANGE_RATE, rate);
    }

    public Result syncDown() {
        // mark the sync result false initially
        Result result = new Result();

        // get the id and local db version for the user
        String id = PrefHelper.getString(appContext, Key.ID, null);
        int dbVersionLocal = PrefHelper.getInt(appContext, Key.LOCAL_DB_VERSION, 0);

        /* construct a POST request with required parameters */
        RequestBody requestBody = new FormBody.Builder(Charsets.UTF_8).add(Key.ID, id).add(Key.SERVER_KEY_VERSION, String.valueOf(dbVersionLocal)).build();
        Request request = new Request.Builder().url(Key.URL_SYNC_DOWN).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();

        try (Response serverResponse = client.newCall(request).execute()) {
            String data = serverResponse.body().string();
            JSONObject reply = new JSONObject(data);

            int serverResult = reply.getInt(Key.SERVER_KEY_CODE);
            if (serverResult != 1) throw new Exception(reply.getString(Key.SERVER_KEY_MESSAGE));

            int dbVersionOnline = reply.getInt(Key.SERVER_KEY_VERSION);

            /* if both versions are the same that means there is no update required */
            if (dbVersionOnline == dbVersionLocal) {
                result.code = Result.CODE_NEUTRAL;
                result.msg = appContext.getString(R.string.sync_message_no_sync_required);
                return result;
            }

            db.beginTransaction();
            for (String table : tables) {
                if (!reply.has(table)) continue;
                JSONArray queries = reply.getJSONArray(table);
                for (int i = 0; i < queries.length(); i++) db.execSQL((String) queries.get(i));
            }
            db.setTransactionSuccessful();

            PrefHelper.putInt(appContext, Key.LOCAL_DB_VERSION, dbVersionOnline);

            result.code = Result.CODE_SUCCESS;
            result.msg = "The local database has been successfully synced with the online one.";
        } catch (Exception e) {
            ErrorRegister.register(e, Updater.class, db);
            result.code = Result.CODE_ERROR;
            result.msg = e.getMessage();
        } finally {
            if (db.inTransaction()) db.endTransaction();
        }
        return result;
    }

    public Result pushUpdate() {
        // mark the sync result false initially
        Result result = new Result();

        /* get the id and db version for the user */
        String id = PrefHelper.getString(appContext, Key.ID, null);
        String version = String.valueOf(PrefHelper.getInt(appContext, Key.LOCAL_DB_VERSION, 0));

        /* get the data to be synced and make sure we have some data to sync */
        String notSyncedData = getNotSyncedData(id);
        if (notSyncedData == null) {
            result.code = Result.CODE_NEUTRAL;
            result.msg = appContext.getString(R.string.sync_message_no_sync_required);
            return result;
        }

        /* construct a POST request with required parameters */
        RequestBody requestBody = new FormBody.Builder(Charsets.UTF_8)
                .add(Key.SERVER_KEY_VERSION, version)
                .add(Key.ID, id)
                .add(Key.SERVER_KEY_DATA, notSyncedData).build();
        Request request = new Request.Builder().url(Key.URL_SYNC_PUSH).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();

        try (Response serverResponse = client.newCall(request).execute()) {
            String data = serverResponse.body().string();
            JSONObject response = new JSONObject(data);

            int serverResult = response.getInt(Key.SERVER_KEY_CODE);
            if (serverResult != 1) throw new Exception(response.getString(Key.SERVER_KEY_MESSAGE));

            db.beginTransaction();

            /* update the sync columns of the records to reflect the push */
            ContentValues values = new ContentValues();
            values.put(Key.SYNC, 1);
            String[] whereArgs = {"0"};

            for (String table : tables) {
                if (!response.has(table)) continue;

                // get the ids of the row for the table to mark the sync OK
                JSONArray ids = response.getJSONArray(table);
                for (int i = 0; i < ids.length(); i++) {
                    whereArgs[0] = (String) ids.get(i);
                    int affected = db.update(table, values, Key.ID + "=?", whereArgs);
                    if (affected != 1) throw new Exception("Failed to update the sync of a record");
                }
            }
            db.setTransactionSuccessful();

            int dbVersion = response.getInt(Key.SERVER_KEY_VERSION);
            PrefHelper.putInt(appContext, Key.LOCAL_DB_VERSION, dbVersion);

            result.code = Result.CODE_SUCCESS;
            result.msg = "Everything has been backed up with the online database.";
        } catch (Exception e) {
            ErrorRegister.register(e, Updater.class, db);
            result.code = Result.CODE_ERROR;
            result.msg = e.getMessage();
        } finally {
            if (db.inTransaction()) db.endTransaction();
        }
        return result;
    }

    public String getNotSyncedData(String id) {
        JSONObject data = new JSONObject();

        Cursor cursor = null;
        try {
            for (String table : tables) { // for each table
                cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s.sync = 0", table, table), null);
                if (cursor == null || cursor.getCount() < 1) continue;

                String[] colNames = cursor.getColumnNames();
                JSONArray tableData = new JSONArray();

                while (cursor.moveToNext()) { // for each row of the table
                    StringBuilder dbColNames = new StringBuilder();
                    StringBuilder dbColValues = new StringBuilder();
                    for (String colName : colNames) { // we are creating INSERT & VALUES parts of query
                        if (colName.equals("sync")) continue;
                        dbColNames.append(colName);
                        getColValue(colName, dbColValues, cursor);
                        dbColNames.append(",");
                        dbColValues.append(",");
                    }

                    /* trim extra commas that are at the end */
                    String colPart = dbColNames.delete(dbColNames.lastIndexOf(","), dbColNames.length()).toString();
                    String valPart = dbColValues.delete(dbColValues.lastIndexOf(","), dbColValues.length()).toString();

                    String query = String.format("INSERT INTO %s%s(%s) VALUES(%s)", table, id, colPart, valPart);
                    tableData.put(query);
                }
                data.put(table, tableData);
            }
        } catch (Exception e) {
            ErrorRegister.register(e, Updater.class, db);
            data = null;
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }

        return (data != null && data.length() > 0) ? data.toString() : null;
    }

    private void getColValue(String colName, StringBuilder record, Cursor cursor) {
        int colIndex = cursor.getColumnIndex(colName);
        int colType = cursor.getType(colIndex);

        if (colType == Cursor.FIELD_TYPE_FLOAT) {
            record.append(cursor.getDouble(colIndex));
        } else if (colType == Cursor.FIELD_TYPE_INTEGER) {
            record.append(cursor.getInt(colIndex));
        } else if (colType == Cursor.FIELD_TYPE_STRING) {
            record.append(String.format("'%s'", cursor.getString(colIndex).replaceAll("'", "''")));
        }
    }

}
