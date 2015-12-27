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

import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.util.Log;

import net.callmeike.android.util.ProcessStats;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class Logger extends ILogger.Stub {
    private static final String TAG = "LOG";
    private static final int PERIOD = 2 * 1000;
    private static final int MSG = -1;

    private static class LogHandler extends Handler {
        private final ProcessStats stats = new ProcessStats();
        private volatile boolean running;

        public void handleMessage(Message msg) {
            if (!running) { return; }
            stats.log("POLL", "===");
            sendEmptyMessageDelayed(MSG, PERIOD);
        }

        public void startLogging() {
            stats.log(TAG, "startLogging");
            running = true;
            sendEmptyMessageDelayed(MSG, PERIOD);
        }

        public void stopLogging() {
            stats.log(TAG, "stopLogging");
            running = false;
        }
    }


    private final ProcessStats stats = new ProcessStats();
    private final LogHandler hdlr = new LogHandler();

    public void startLogging() {
        stats.log(TAG, "start");
        hdlr.startLogging();
    }

    public void stopLogging() {
        stats.log(TAG, "stop");
        hdlr.stopLogging();
    }
}
