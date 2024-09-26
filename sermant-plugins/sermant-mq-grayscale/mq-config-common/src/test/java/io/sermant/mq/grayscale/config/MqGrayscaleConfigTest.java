/*
 * Copyright (C) 2024-2024 Sermant Authors. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.sermant.mq.grayscale.config;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * MqGrayscaleConfig test
 *
 * @author chengyouling
 * @since 2024-09-18
 **/
public class MqGrayscaleConfigTest {
    @Test
    public void testGetGrayTagsByServiceMeta() {
        MqGrayscaleConfig config = CommonConfigUtils.getMqGrayscaleConfig();
        Map<String, String> properties = new HashMap<>();
        properties.put("x_lane_tag", "gray" );
        Assert.assertSame(1, config.getGrayTagsByServiceMeta(properties).size());
    }
}