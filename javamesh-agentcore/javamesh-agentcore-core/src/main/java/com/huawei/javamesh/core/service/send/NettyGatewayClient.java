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

package com.huawei.javamesh.core.service.send;

import com.huawei.javamesh.core.common.LoggerFactory;
import com.huawei.javamesh.core.lubanops.bootstrap.config.AgentConfigManager;
import com.huawei.javamesh.core.lubanops.integration.transport.ClientManager;
import com.huawei.javamesh.core.lubanops.integration.transport.netty.client.NettyClient;
import com.huawei.javamesh.core.lubanops.integration.transport.netty.pojo.Message;

import java.util.logging.Logger;

/**
 * 基于Netty Client的网关发送服务
 */
public class NettyGatewayClient implements GatewayClient {

    private static final Logger LOGGER = LoggerFactory.getLogger();

    private NettyClient nettyClient;

    @Override
    public void start() {
        nettyClient = ClientManager.getNettyClientFactory().getNettyClient(
                AgentConfigManager.getNettyServerIp(),
                Integer.parseInt(AgentConfigManager.getNettyServerPort()));
    }

    @Override
    public void stop() {
        // close nettyClient
    }

    @Override
    public void send(byte[] data, int typeNum) {
        Message.ServiceData.DataType dataType = Message.ServiceData.DataType.forNumber(typeNum);
        if (dataType == null) {
            LOGGER.severe("Wrong type of data.");
            return;
        }
        nettyClient.sendData(data, dataType);
    }
}
