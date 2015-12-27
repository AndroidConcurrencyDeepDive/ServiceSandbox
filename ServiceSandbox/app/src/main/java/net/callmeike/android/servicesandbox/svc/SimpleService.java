/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.callmeike.android.servicesandbox.svc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.callmeike.android.util.ProcessStats;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class SimpleService extends Service {
    private static final String TAG = "SVC";


    private final ProcessStats stats = new ProcessStats();
    private Logger logger;

    @Override
    public void onCreate() {
        stats.log(TAG, "created");
        logger = new Logger();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stats.log(TAG, "bind");
        return logger;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stats.log(TAG, "start");
        logger.startLogging();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stats.log(TAG, "unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        stats.log(TAG, "rebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        stats.log(TAG, "destroy");
        logger.stopLogging();
        super.onDestroy();
    }
}
