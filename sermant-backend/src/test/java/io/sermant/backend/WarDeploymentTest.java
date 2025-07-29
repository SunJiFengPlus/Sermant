/*
 * Copyright (C) 2025-2025 Sermant Authors. All rights reserved.
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

package io.sermant.backend;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for WAR deployment functionality
 *
 * @author sermant
 * @since 2025-01-01
 */
public class WarDeploymentTest {

    @Test
    public void testWarDeploymentConfiguration() {
        Backend backend = new Backend();
        
        assertThat(backend).isNotNull();
    }
    
    @Test
    public void testWarDeploymentCondition() {
        WarDeploymentCondition condition = new WarDeploymentCondition();
        
        // Test with system property
        System.setProperty("sermant.deployment.mode", "war");
        assertThat(condition.matches(null, null)).isTrue();
        
        System.setProperty("sermant.deployment.mode", "jar");
        assertThat(condition.matches(null, null)).isFalse();
        
        // Clean up
        System.clearProperty("sermant.deployment.mode");
    }

    @Test
    public void testMainMethodExecution() {
        assertThat(Backend.class.getDeclaredMethods())
                .anyMatch(method -> method.getName().equals("main"));
    }

    @Test
    public void testConfigureMethodExists() {
        assertThat(WarDeploymentConfig.class.getDeclaredMethods())
                .anyMatch(method -> method.getName().equals("configure"));
    }
} 