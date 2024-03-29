/**
 * Copyright (C) 2016 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.rpc.netty;

import com.google.common.reflect.AbstractInvocationHandler;

import java.lang.reflect.Method;
import java.util.UUID;

import com.newlandframework.rpc.core.MessageCallBack;
import com.newlandframework.rpc.model.MessageRequest;

/**
 * Rpc客户端消息处理
 * @author tangjie<https://github.com/tang-jie>
 * @filename:MessageSendProxy.java
 * @description:MessageSendProxy功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class MessageSendProxy<T> extends AbstractInvocationHandler {

    @Override
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParametersVal(args);

        MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = handler.sendRequest(request);
        return callBack.start();
    }
}

