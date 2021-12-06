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

package com.huawei.javamesh.core.lubanops.integration.transport.netty.client;

import com.huawei.javamesh.core.lubanops.integration.transport.netty.common.BaseHandler;
import com.huawei.javamesh.core.lubanops.integration.transport.netty.pojo.Message;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * client消息处理类
 *
 * @author lilai
 * @version 0.0.1
 * @since 2021-08-07
 */
public class ClientHandler extends BaseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private NettyClient client;

    public ClientHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    protected void handlerData(ChannelHandlerContext ctx, Message.NettyMessage msg) {
    }

    @Override
    protected void handlerAllIdle(ChannelHandlerContext ctx) {
        super.handlerAllIdle(ctx);
        sendPingMsg(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.error("Server channel is inaction");
        client.doConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Exception occurs. Exception info: {}", cause);
    }
}
