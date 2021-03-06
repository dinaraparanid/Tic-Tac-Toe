package com.dinaraparanid.tictactoe;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dinaraparanid.tictactoe.utils.polymorphism.Player;

import org.jetbrains.annotations.Contract;

public final class MainApplication extends Application {

    static {
        System.loadLibrary("tictactoe");
    }

    private static final native void initNativeLogger();

    @Contract(pure = true)
    public final boolean isServiceBound() { return serviceBound; }

    boolean serviceBound = false;

    @NonNull
    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public final void onServiceConnected(
                @NonNull final ComponentName name,
                @NonNull final IBinder service
        ) {
            Log.d("Server", "Server connected");
            serviceBound = true;
        }

        @Override
        public final void onServiceDisconnected(@NonNull final ComponentName name) {
            Log.d("Server", "Service disconnected");
            serviceBound = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Player.ApplicationAccessor.application = this;
        initNativeLogger();
    }
}
