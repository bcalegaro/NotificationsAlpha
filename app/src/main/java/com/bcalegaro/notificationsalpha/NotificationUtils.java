package com.bcalegaro.notificationsalpha;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager _notificationManager;
    private Context _context;

    private void createChannel() {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
    }

    //construtor da classe, precisa receber o Contexto para obter acesso aos recursos do Android
    //em nome do aplicativo atual
    public NotificationUtils(Context base) {
        super(base);
        //armazena o contexto em uma variável para acesso futuro
        _context = base;
        //cria um canal de notificação
        createChannel();
    }

    public NotificationCompat.Builder setNotification(String title, String body) {
        //declara a intenção de abrir atividade principal (será atividade quando de clicar sobre a notificação)
        Intent resultIntent = new Intent(_context, MainActivity.class);
        // exemplo de como passar informações entre as notificações e o aplicativo
        resultIntent.putExtra("exemplo", "My Med");
        // TaskStackbuilder cria uma pilha de atividade e adiciona a atividade deseja ao topo
        // de acordo com hierarquia de atividade declaradas no arquivo de manifesto
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(_context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Cria uma Pending Intent com as informações configuradas na pilha
        // PendingIntent será necessária para o método setContentIntent() da notificação
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(4, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //efetivamente constroi a notificação
        return new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)    //icone da notificação
                .setContentTitle(title)                             //titulo
                .setContentText(body)                               //conteudo
                .setAutoCancel(true)                                //ativa a opção de desativar a notificação com o toque
                .setContentIntent(resultPendingIntent)              //ao clicar na notificação encaminha para a ação tal
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);  //definição da prioridade (padrão, alta, urgente)
    }


    //método simples para retornar uma instância do serviço do sistema Android - Notification Manager
    public NotificationManager getManager() {
        if (_notificationManager == null) {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return _notificationManager;
    }


    //método exemplo - cria um alarme para disparar daqui a 10 segundos
    public void rememberMeInTenSeconds() {
        //Obtem acesso ao serviço do Android de Alarms
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Declara a Intent para encaminhar as solicitaçõs para a classe ReminderBroadcast
        Intent intent = new Intent(this, ReminderBroadcast.class);
        //Exemplo de como passar informações de um lado para outro
        intent.putExtra("data", "exemplo");
        //PedingIntent são declarações para serem repassadas a outras partes do Android para executar alguma coisa
        //neste caso, serão repassadas para o AlarmManager realizar alguma ação
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                0, //DEVE SER UM CÓDIGO ÚNICO PARA CADA ALARME CONFIGURADO
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //Descobre a hora atual e adiciona 10 segundos
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 10;
        long _triggerReminder = _currentTime + tenSeconds;
        //Configura o alarm para disparar no tempo determinado
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, //uma das possíveis opções de alarmes, dispara mesmo com a tela desligada (acorda celular)
                _triggerReminder,        //alarm dispara daqui a quanto tempo
                pendingIntent);          //qual ação vai acontecer ao disparar o alarm, nesse caso a ação vai acionar o ReminderBroadcast
    }
}