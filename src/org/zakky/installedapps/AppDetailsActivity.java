/*
 * Copyright 2011 YAMAZAKI Makoto<makoto1975@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zakky.installedapps;

import android.app.Activity;
import android.os.Bundle;

/**
 *
 */
public final class AppDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    /*
            ActivityInfo activityInfo = info.activityInfo;
            int icon = info.icon;
            boolean isDefault = info.isDefault;
            int labelRes = info.labelRes;
            int match = info.match;
            CharSequence nonLocalizedLabel = info.nonLocalizedLabel;
            int preferredOrder = info.preferredOrder;
            int priority = info.priority;
            // API level 5
            //String resolvePackageName = info.resolvePackageName;
            ServiceInfo serviceInfo = info.serviceInfo;
            int specificIndex = info.specificIndex;

            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            info.dump(new Printer() {
                @Override
                public void println(String x) {
                    pw.println(x);
                }
            }, "");

            pw.flush();
            final String infoStr = sw.toString();
            infoMap.put(Keys.DUMP, infoStr);
     */

}
