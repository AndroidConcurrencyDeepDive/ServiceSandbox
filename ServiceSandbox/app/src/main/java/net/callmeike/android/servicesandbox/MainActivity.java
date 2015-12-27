package net.callmeike.android.servicesandbox;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.callmeike.android.servicesandbox.svc.ILogger;
import net.callmeike.android.util.ProcessStats;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "MAIN";


    private ProcessStats stats = new ProcessStats();
    private ILogger svc;

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        stats.log(TAG, "connected: " + name + ", " + binder);
        ILogger service = ILogger.Stub.asInterface(binder);
        if (null != service) {
            try { service.startLogging(); }
            catch (RemoteException e) { Log.e(TAG, "failed starting service"); }
        }
        svc = service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        stats.log(TAG, "disconnected: " + name);
        svc = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stats.log(TAG, "created");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start)
    void startSvc() {
        stats.log(TAG, "start service");
        startService(getServiceIntent());
    }

    @OnClick(R.id.stop)
    void stopSvc() {
        stats.log(TAG, "stop service");
        stopService(getServiceIntent());
    }

    @OnClick(R.id.bind)
    void bindSvc() {
        stats.log(TAG, "bind service");
        bindService(getServiceIntent(), this, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.unbind)
    void unbindSvc() {
        stats.log(TAG, "unbind service");
        if (null != svc) {
            try { svc.stopLogging(); }
            catch (RemoteException e) { Log.e(TAG, "failed stopping service"); }
            unbindService(this);
        }
        svc = null;
    }

    @NonNull
    private Intent getServiceIntent() {
        Intent intent = new Intent();
        //intent.setPackage("net.callmeike.android.servicesandbox");
        intent.setAction("net.callmeike.android.servicesandbox.LOGGER");
        return intent;
    }
}
