/*
 * Copyright (C) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved
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

package com.huawei.flowrecord.utils;

/**
 * 获取appname使用
 *
 */
public final class AppNameUtil {
    /**
     * 获取appname
     */
    public static final String APP_NAME = "project.name";

    private static String appName;

    private AppNameUtil() {
    }

    static {
        resolveAppName();
    }

    public static void resolveAppName() {
        String app = System.getProperty(APP_NAME);

        // use -Dproject.name first
        if (!StringUtil.isEmpty(app)) {
            appName = app;
        } else {
            appName = "default";
        }
        return;
    }

    public static String getAppName() {
        return appName;
    }
}
