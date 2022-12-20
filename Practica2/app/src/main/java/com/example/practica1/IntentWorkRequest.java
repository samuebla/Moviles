package com.example.practica1;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Map;

//Realiza una tarea, es llamado por el workManager
public class IntentWorkRequest extends Worker {
    NotificationCompat.Builder notificationBuilder;
    Context contextActivity;
    NotificationManagerCompat notificationManager;
    public IntentWorkRequest(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contextActivity = context;
    }

    //Tarea a realizar cuando toque
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = this.getInputData();

        //Obtenemos el input y los valores necesarios
        Map<String, Object> dataValues = inputData.getKeyValueMap();
        String channelId = (String) dataValues.get("channelId");
        String contentText = (String) dataValues.get("contentText");
        String contentTitle = (String) dataValues.get("contentTitle");
        Integer notificationId = (Integer) dataValues.get("notificationId");

        //Obtenermos el noticationManager del contexto
        notificationManager = NotificationManagerCompat.from(this.contextActivity);

        //Inicializamos un intent que nos permita lanzar nuestra propia actividad
        Intent activityIntent = new Intent(this.contextActivity, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent que permite que el intent guardado no se haga inmediatamente sino que se pueda añadir a una notificacion por ejemplo y ser lanzado por ella en vez de por el Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this.contextActivity, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE);
        //Creamos una notificacion y guardamos el pendingIntent para que lo haga si el usuario hace click
        notificationBuilder = new NotificationCompat.Builder(this.contextActivity, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle( contentTitle )
                .setContentText( contentText)
                .setStyle( new NotificationCompat.BigTextStyle()
                        .bigText( contentText ))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat. PRIORITY_DEFAULT);

        //Mandamos la notificación
        notificationManager.notify(notificationId, notificationBuilder.build());

        return Result.success();
    }
}
