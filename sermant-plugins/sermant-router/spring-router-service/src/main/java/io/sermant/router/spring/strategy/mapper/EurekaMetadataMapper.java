/*
 * Copyright (C) 2021-2022 Huawei Technologies Co., Ltd. All rights reserved.
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

package io.sermant.router.spring.strategy.mapper;

import io.sermant.router.common.utils.ReflectUtils;
import io.sermant.router.spring.utils.SpringRouterUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Obtain mapper from metadata
 *
 * @author provenceee
 * @since 2021-10-14
 */
public class EurekaMetadataMapper extends AbstractMetadataMapper<Object> {
    @Override
    public Map<String, String> apply(Object obj) {
        return SpringRouterUtils.getMetadata(ReflectUtils.invokeWithNoneParameter(obj, "getInstanceInfo"));
    }

    @Override
    public List<String> getName() {
        return Arrays.asList("org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServer",
                "com.netflix.niws.loadbalancer.DiscoveryEnabledServer");
    }
}