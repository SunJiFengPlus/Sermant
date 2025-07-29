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

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition to check if WAR deployment mode should be enabled
 *
 * @author sermant
 * @since 2025-01-01
 */
public class WarDeploymentCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Check if we should enable WAR deployment mode
        // This can be controlled by system property or environment variable
        String deploymentMode = System.getProperty("sermant.deployment.mode");
        if (deploymentMode == null) {
            deploymentMode = System.getenv("SERMANT_DEPLOYMENT_MODE");
        }
        
        // If explicitly set to "war", enable WAR deployment
        if ("war".equalsIgnoreCase(deploymentMode)) {
            return true;
        }
        
        // If explicitly set to "jar", disable WAR deployment
        if ("jar".equalsIgnoreCase(deploymentMode)) {
            return false;
        }
        
        // Default behavior: check if running in a servlet container
        // by looking for ServletContext bean (only available in servlet containers)
        try {
            context.getBeanFactory().getBean("servletContext");
            return true;
        } catch (Exception e) {
            // If ServletContext bean is not available, we're running standalone
            return false;
        }
    }
} 