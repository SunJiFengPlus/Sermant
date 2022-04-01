/*
 * Copyright (C) 2022-2022 Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huawei.registry.support;

import java.lang.reflect.Field;
import java.security.PrivilegedAction;
import java.util.function.Supplier;

/**
 * 字段访问器设置
 *
 * @author zhouss
 * @since 2022-02-24
 */
public class FieldAccessAction implements PrivilegedAction<Object> {
    private final Field field;

    private final Supplier<Object> supplier;

    /**
     * 构造器
     *
     * @param field 字段
     */
    public FieldAccessAction(Field field) {
        this(field, null);
    }

    /**
     * 构造器
     *
     * @param field 字段
     * @param supplier 拓展值
     */
    public FieldAccessAction(Field field, Supplier<Object> supplier) {
        this.field = field;
        this.supplier = supplier;
    }

    @SuppressWarnings("checkstyle:RegexpSingleline")
    @Override
    public Object run() {
        if (field != null) {
            field.setAccessible(true);
        }
        if (supplier != null) {
            return supplier.get();
        }
        return null;
    }
}