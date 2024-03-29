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
package com.newlandframework.test;

import com.newlandframework.rpc.services.PersonManage;
import com.newlandframework.rpc.services.pojo.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:PojoCallTest.java
 * @description:PojoCallTest功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/11/7
 */
public class PojoCallTest {
    public static void main1(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        PersonManage manage = (PersonManage) context.getBean("personManage");

        Person p = new Person();
        p.setId(20150811);
        p.setName("XiaoHaoBaby");
        p.setAge(1);

        int result = manage.save(p);

        manage.query(p);

        System.out.println("call pojo rpc result:" + result);

        context.destroy();
    }

    public static void main(String[] args) {
        List<String> lst = getClassMethodSignature(PersonManage.class);
        System.out.println(lst);
    }

    public static List<String> getClassMethodSignature(Class<?> cls) {
        List<String> list = new ArrayList<String>();
        if (cls.isInterface()) {
            Method[] methods = cls.getDeclaredMethods();
            StringBuilder signatureMethod = new StringBuilder();
            for (Method member : methods) {
                int modifiers = member.getModifiers();
                if (Modifier.isAbstract(modifiers) && Modifier.isPublic(modifiers)) {
                    signatureMethod.append(modifiers(Modifier.PUBLIC));
                    if (Modifier.isFinal(modifiers)) {
                        signatureMethod.append(modifiers(Modifier.FINAL));
                    }
                } else {
                    signatureMethod.append(modifiers);
                }

                if (member instanceof Method) {
                    signatureMethod.append(getType(((Method) member).getReturnType()) + " ");
                }

                signatureMethod.append(member.getName() + "(");
                signatureMethod.append(getClassType(member.getParameterTypes()));
                signatureMethod.append(")");
                Class<?>[] exceptions = member.getExceptionTypes();
                if (exceptions.length > 0) {
                    signatureMethod.append(" throws ");
                }
                listTypes(exceptions);
                signatureMethod.append(";");
                list.add(signatureMethod.toString());
                signatureMethod.delete(0, signatureMethod.length());
            }
        }
        return list;
    }

    private static String modifiers(int m) {
        return m != 0 ? Modifier.toString(m) + " " : "";
    }

    private static String getType(Class<?> t) {
        String brackets = "";
        while (t.isArray()) {
            brackets += "[]";
            t = t.getComponentType();
        }
        return t.getName() + brackets;
    }

    private static String getClassType(Class<?>[] types) {
        StringBuilder type = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                type.append(", ");
            }
            type.append(getType(types[i]));
        }
        return type.toString();
    }

    private static void listTypes(Class<?>[] types) {
        StringBuilder provider = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                provider.append(", ");
            }
            provider.append(getType(types[i]));
        }
    }
}

