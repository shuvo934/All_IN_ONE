package com.shuvo.ttit.qrcodetester;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FlashAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            Intent intent = new Intent(context, FlashlightWidgetReceiver.class);
            intent.setAction("COM_FLASHLIGHT_WIDGET");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_layout_widget);
            views.setOnClickPendingIntent(R.id.example_widget_button,pendingIntent);
            views.setImageViewResource(R.id.example_widget_button, R.drawable.flashlight_off_24);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
