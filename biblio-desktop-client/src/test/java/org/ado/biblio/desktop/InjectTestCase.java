package org.ado.biblio.desktop;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author Andoni del Olmo
 * @since 19.02.15
 */
public abstract class InjectTestCase<T> {

    protected T unitUnderTest;

    protected abstract void setUp() throws Exception;

    @Before
    public void setUpInjectedTestCase() throws Exception {
        MockitoAnnotations.initMocks(this);
        setUp();
        try {
            Class<?> aClass = Class.forName(getClassCanonicalName());
            Object o = aClass.newInstance();
            unitUnderTest = (T) o;
            initialize(unitUnderTest);
        } catch (Exception e) {
            // ignore
        }
    }

    @After
    public void tearDownInjectedTestCase() {
        destroy(unitUnderTest);
    }

    void initialize(Object instance) {
        Class<? extends Object> clazz = instance.getClass();
        invokeMethodWithAnnotation(clazz, instance, PostConstruct.class);
    }

    void destroy(Object instance) {
        Class<? extends Object> clazz = instance.getClass();
        invokeMethodWithAnnotation(clazz, instance, PreDestroy.class);
    }

    private String getClassCanonicalName() {
        return getTypeForClassUnderTest().toString().replace("class ", "");
    }

    private Type getTypeForClassUnderTest() {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected void invokeMethodWithAnnotation(Class clazz, final Object instance, final Class<? extends Annotation> annotationClass) throws IllegalStateException, SecurityException {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                AccessController.doPrivileged((PrivilegedAction) () -> {
                    boolean wasAccessible = method.isAccessible();
                    try {
                        method.setAccessible(true);
                        return method.invoke(instance);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        throw new IllegalStateException("Problem invoking " + annotationClass + " : " + method, ex);
                    } finally {
                        method.setAccessible(wasAccessible);
                    }
                });
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            invokeMethodWithAnnotation(superclass, instance, annotationClass);
        }
    }
}