package net.callmeike.android.serviceclient;

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
import android.view.ViewGroup;
import android.widget.CheckBox;

import net.callmeike.android.servicesandbox.svc.ILogger;
import net.callmeike.android.util.ProcessStats;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "MAIN";


    private final ProcessStats stats = new ProcessStats();
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
        stats.log(TAG, "create");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        stats.log(TAG, "start");
        super.onStart();
    }

    @Override
    protected void onResume() {
        stats.log(TAG, "resume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        stats.log(TAG, "pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        stats.log(TAG, "stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stats.log(TAG, "destroy");
        super.onDestroy();
    }

    @OnClick(R.id.bind)
    void bindSvc() {
        int flags = 0;

        ViewGroup checkboxes = (ViewGroup) findViewById(R.id.checkboxes);
        int children = checkboxes.getChildCount();
        for (int i = 0; i < children; i++) {
            CheckBox box = (CheckBox) checkboxes.getChildAt(i);
            if (!box.isChecked()) { continue; }
            int id = box.getId();
            switch (id) {
                case R.id.autoCreate:
                    flags |= Context.BIND_AUTO_CREATE;
                    break;
                case R.id.debug:
                    flags |= Context.BIND_DEBUG_UNBIND;
                    break;
                case R.id.notForeground:
                    flags |= Context.BIND_NOT_FOREGROUND;
                    break;
                case R.id.aboveClient:
                    flags |= Context.BIND_ABOVE_CLIENT;
                    break;
                case R.id.manageOom:
                    flags |= Context.BIND_ALLOW_OOM_MANAGEMENT;
                    break;
                case R.id.waivePriority:
                    flags |= Context.BIND_WAIVE_PRIORITY;
                    break;
                case R.id.important:
                    flags |= Context.BIND_IMPORTANT;
                    break;
                case R.id.withActivity:
                    flags |= Context.BIND_ADJUST_WITH_ACTIVITY;
                    break;
                default:
                    Log.w(TAG, "unrecognized checkbox: " + id);
            }
        }

        stats.log(TAG, "bind service: " + Integer.toHexString(flags));
        bindService(getServiceIntent(), this, flags);
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
        intent.setPackage("net.callmeike.android.servicesandbox");
        intent.setAction("net.callmeike.android.servicesandbox.LOGGER");
        return intent;
    }
}
