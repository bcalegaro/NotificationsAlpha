package com.bcalegaro.notificationsalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener((view -> rememberMe()));
    }

    public void rememberMe() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //Necessário por questões de compatibilidade
            NotificationUtils notificationUtils = new NotificationUtils(getBaseContext());
            notificationUtils.rememberMeInTenSeconds(); //me lembre daqui a 10s
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Exemplo de leitura de parametros vindo ao se clicar em uma notificação
        String param = getIntent().getStringExtra("exemplo");
        if (param != null) {
            Log.d("NOTI-ALPHA", "Recebeu da notificação o valor: " + param);
            Toast.makeText(this, "Recebeu da notificação o valor: " + param, Toast.LENGTH_SHORT).show();
            //Remove para a informação não aparecer novamente
            getIntent().removeExtra("exemplo");
        }
    }
}