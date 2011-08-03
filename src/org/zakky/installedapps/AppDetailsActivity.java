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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

        addLine(data, "priority", "" + info.priority);
        addLine(data, "nonLocalizedLabel", info.nonLocalizedLabel);

        final ActivityInfo activityInfo = info.activityInfo;
        addLine(data, "act_permission", activityInfo.permission);
        addLine(data, "act_processName", activityInfo.processName);
        addLine(data, "act_targetActivity", activityInfo.targetActivity);
        addLine(data, "act_taskAffinity", activityInfo.taskAffinity);
        addLine(data, "act_configChanges", "0x" + Integer.toHexString(activityInfo.configChanges));
        addLine(data, "act_enabled", Boolean.toString(activityInfo.enabled));
        addLine(data, "act_exported", Boolean.toString(activityInfo.exported));
        addLine(data, "act_flags", "0x" + Integer.toHexString(activityInfo.flags));
        addLine(data, "act_launchMode", "0x" + Integer.toHexString(activityInfo.launchMode));
        addLine(data, "act_nonLocalizedLabel", activityInfo.nonLocalizedLabel);
        addLine(data, "act_screenOrientation",
                "0x" + Integer.toHexString(activityInfo.screenOrientation));
        addLine(data, "act_softInputMode", "0x" + Integer.toHexString(activityInfo.softInputMode));
        addLine(data, "act_theme", "" + activityInfo.theme);

        final ApplicationInfo applicationInfo = activityInfo.applicationInfo;
        addLine(data, "act_app_name", applicationInfo.name);
        addLine(data, "act_app_nonLocalizedLabel", applicationInfo.nonLocalizedLabel);
        addLine(data, "act_app_packageName", applicationInfo.packageName);
        addLine(data, "act_app_className", applicationInfo.className);
        addLine(data, "act_app_dataDir", applicationInfo.dataDir);
        addLine(data, "act_app_manageSpaceActivityName", applicationInfo.manageSpaceActivityName);
        addLine(data, "act_app_permission", applicationInfo.permission);
        addLine(data, "act_app_processName", applicationInfo.processName);
        addLine(data, "act_app_publicSourceDir", applicationInfo.publicSourceDir);
        addLine(data, "act_app_sourceDir", applicationInfo.sourceDir);
        addLine(data, "act_app_taskAffinity", applicationInfo.taskAffinity);
        addLine(data, "act_app_enabled", Boolean.toString(applicationInfo.enabled));
        addLine(data, "act_app_flags", "0x" + Integer.toHexString(applicationInfo.flags));
        addLine(data, "act_app_sharedLibraryFiles",
                Arrays.toString(applicationInfo.sharedLibraryFiles));
        addLine(data, "act_app_targetSdkVersion", "" + applicationInfo.targetSdkVersion);
        addLine(data, "act_app_theme", "" + applicationInfo.theme);
        addLine(data, "act_app_uid", "" + applicationInfo.uid);

        return data;
    }

    private void addLine(List<Map<String, Object>> data, String header, CharSequence value) {
        final Map<String, Object> m = new HashMap<String, Object>();
        final String line = header + ": " + value;
        m.put(KEY, line);
        data.add(m);
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
