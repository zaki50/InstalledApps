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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public final class InstalledAppsActivity extends ListActivity {

    private List<Map<String, Object>> mAppList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (getLastNonConfigurationInstance() != null) {
            @SuppressWarnings("unchecked")
            final List<Map<String, Object>> temp = //
            (List<Map<String, Object>>) getLastNonConfigurationInstance();
            mAppList = temp;
        } else {
            final List<ResolveInfo> apps = getInstalledApplications();
            mAppList = makeApplicationList(apps);
            // TODO 別スレッドでロードするようにする
            loadLabel(mAppList);
            sortByLabel(mAppList);
        }

        final SimpleAdapter adapter = new SimpleAdapter(this, mAppList, R.layout.app_row,
                new String[] {
                        Keys.ICON, Keys.LABEL
                }, new int[] {
                        R.id.icon, R.id.label
                });
        adapter.setViewBinder(new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                switch (view.getId()) {
                    case R.id.icon:
                        if (data instanceof Bitmap) {
                            final ImageView v = (ImageView) view;
                            v.setImageBitmap((Bitmap) data);
                        }
                        return true;
                    case R.id.label: {
                        final TextView v = (TextView) view;
                        if (data instanceof CharSequence) {
                            v.setText((CharSequence) data);
                        }
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            Toast.makeText(this, "aaa", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mAppList;
    }

    /**
     * アプリケーション一覧をパッケージマネージャから取得します。
     * <p>
     * アイコンやラベルなどロードに時間のかかるものは別途取得する必要があります。
     * </p>
     * @return ACTION_MAIN で CATEGORY_LAUNCHER なアクティビティの一覧。
     */
    private List<ResolveInfo> getInstalledApplications() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
        return apps;
    }

    /**
     * リストの各row のデータを保持する {@link Map} のキー。
     */
    private static final class Keys {
        public static final String RESOLVE_INFO = "resolveInfo";
        public static final String PACKAGE_NAME = "package";
        public static final String ACTIVITY = "activity";

        public static final String LABEL = "label";

        public static final String ICON = "icon";
    }

    private List<Map<String, Object>> makeApplicationList(List<ResolveInfo> apps) {
        final List<Map<String, Object>> result;
        result = new ArrayList<Map<String, Object>>(apps.size());
        for (ResolveInfo info : apps) {
            final Map<String, Object> infoMap = new HashMap<String, Object>();
            result.add(infoMap);
            infoMap.put(Keys.RESOLVE_INFO, info);

            infoMap.put(Keys.PACKAGE_NAME, info.activityInfo.packageName);
            infoMap.put(Keys.ACTIVITY, info.activityInfo.name);
        }
        return result;
    }

    private void loadLabel(List<Map<String, Object>> appList) {
        final PackageManager pm = getPackageManager();
        for (Map<String, Object> app : appList) {
            final ResolveInfo info = (ResolveInfo) app.get(Keys.RESOLVE_INFO);
            if (info == null) {
                continue;
            }

            final CharSequence label = info.loadLabel(pm);
            app.put(Keys.LABEL, label);

            final BitmapDrawable icon = (BitmapDrawable) info.activityInfo.loadIcon(pm);
            app.put(Keys.ICON, icon.getBitmap());
        }
    }

    private void sortByLabel(List<Map<String, Object>> appList) {
        Collections.sort(appList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> info1, Map<String, Object> info2) {
                final String label1 = (String) info1.get(Keys.LABEL);
                final String label2 = (String) info2.get(Keys.LABEL);
                if (label1 == label2) {
                    return 0;
                }
                if (label1 == null) {
                    return -1;
                }
                if (label2 == null) {
                    return 1;
                }
                return label1.compareTo(label2);
            }
        });
    }
}
