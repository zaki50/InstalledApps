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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 *
 */
public final class AppDetailsActivity extends ListActivity {

    public static final String EXTRA_PACKAGE_NAME = "package";

    public static final String EXTRA_CLASS_NAME = "class";

    private ImageView mIcon;

    private TextView mLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_details);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mIcon = (ImageView) findViewById(R.id.icon);
        mLabel = (TextView) findViewById(R.id.label);

        updateViews();
    }

    private static final String KEY = "k";

    private boolean updateViews() {
        final Intent intent = getIntent();
        if (intent == null) {
            return false;
        }
        final ResolveInfo info = getResolveInfo(intent.getStringExtra(EXTRA_PACKAGE_NAME),
                intent.getStringExtra(EXTRA_CLASS_NAME));
        if (info == null) {
            return false;
        }

        final PackageManager pm = getPackageManager();
        final Drawable icon = info.loadIcon(pm);
        final CharSequence label = info.loadLabel(pm);

        mIcon.setImageDrawable(icon);
        mLabel.setText(label);

        final List<Map<String, Object>> data = createData(info);
        setListAdapter(new SimpleAdapter(this, data, android.R.layout.simple_list_item_1,
                new String[] {
                    KEY
                }, new int[] {
                    android.R.id.text1
                }));

        return true;
    }

    private List<Map<String, Object>> createData(ResolveInfo info) {
        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        Map<String, Object> d1 = new HashMap<String, Object>();
        d1.put(KEY, "hoge");
        data.add(d1);

        Map<String, Object> d2 = new HashMap<String, Object>();
        d2.put(KEY, "fuga");
        data.add(d2);

        Map<String, Object> d3 = new HashMap<String, Object>();
        d3.put(KEY, "buroro");
        data.add(d3);

        Map<String, Object> d4 = new HashMap<String, Object>();
        d4.put(KEY, "buroro");
        data.add(d4);

        Map<String, Object> d5 = new HashMap<String, Object>();
        d5.put(KEY, "buroro");
        data.add(d5);

        final ActivityInfo activityInfo = info.activityInfo;
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

        return data;
    }

    private ResolveInfo getResolveInfo(String packageName, String className) {
        if (packageName == null || className == null) {
            return null;
        }
        final PackageManager pm = getPackageManager();
        final Intent intent = new Intent();
        intent.setClassName(packageName, className);

        final List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        if (activities.isEmpty()) {
            return null;
        }
        return activities.get(0);
    }

}
