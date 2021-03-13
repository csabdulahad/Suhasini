package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.helper.InputChecker;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.ProgressDialog;
import net.abdulahad.suhasini.thread.ExeSupplier;

import org.json.JSONObject;

import kotlin.text.Charsets;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    EditText etId, etPass;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating credential...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        etId = findViewById(R.id.id);
        etPass = findViewById(R.id.password);
    }

    public void login(View view) {
        ViewHelper.hideSoftKeyboard(this, ViewHelper.getRootView(this));
        if (!InputChecker.passNumberCheck(etId, false)) return;
        if (!InputChecker.passEmptyCheck(etPass)) return;

        String id = etId.getText().toString();
        String pass = etPass.getText().toString();

        progressDialog.show();
        ExeSupplier.get().lightBGThread().execute(() -> {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder(Charsets.UTF_8)
                    .add("id", id)
                    .add("pass", pass).build();

            Request request = new Request.Builder()
                    .url(Key.URL_ACCOUNT_CHECK)
                    .post(requestBody).build();

            try (Response response = client.newCall(request).execute()) {
                String data = response.body().string();
                Log.d("ahad", "input " + data);
                JSONObject reply = new JSONObject(data);
                int result = reply.getInt("code");
                ExeSupplier.get().UIThread().execute(() -> showToUI(id, result));
            } catch (Exception e) {
                ErrorRegister.register(e, Login.class, Suhasini.getDB(getApplicationContext()));
            }
        });
    }

    private void showToUI(String id, int result) {
        progressDialog.dismiss();
        if (result != 1) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            return;
        }

        PrefHelper.putBoolean(this, Key.LOGGED_IN, true);
        PrefHelper.putString(this, Key.ID, id);

        finish();
        startActivity(new Intent(this, NewLockActivity.class));
    }

}