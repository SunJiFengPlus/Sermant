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

package com.huawei.gray.dubbo.strategy.rule;

import com.huawei.gray.dubbo.strategy.RuleStrategy;
import com.huawei.route.common.gray.label.entity.Route;

import org.apache.dubbo.rpc.Invocation;

import java.util.List;

/**
 * 上游传递标签匹配
 *
 * @author pengyuyi
 * @date 2021/10/15
 */
public class UpstreamRuleStrategy implements RuleStrategy {
    @Override
    public String getTargetServiceIp(List<Route> list, String targetService, String interfaceName, String version,
            Invocation invocation) {
        return null;
        // 使用上游传递版本，暂时屏蔽
        /* String tagVersion;
        CurrentTag currentTag = JSONObject
                .parseObject(invocation.getAttachment(GrayConstant.GRAY_TAG), CurrentTag.class);
        if (currentTag != null && StringUtils.isNotBlank(currentTag.getVersion())) {
            tagVersion = currentTag.getVersion();
        } else {
            tagVersion = LabelCache.getLabel(DubboCache.getAppName()).getCurrentTag()
                    .getValidVersion(DubboCache.getAppName());
        }
        Instances instances = AddrCache
                .getAddr(targetService, RouterUtil.getLdc(invocation), tagVersion, DubboCache.getAppName());
        return RouterUtil.getTargetAndSetAttachment(instances, invocation, tagVersion, RouterUtil.getLdc(invocation));
        */
    }
}
