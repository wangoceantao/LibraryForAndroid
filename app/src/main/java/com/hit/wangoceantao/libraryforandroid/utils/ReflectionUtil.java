package com.hit.wangoceantao.libraryforandroid.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static boolean isSupportMethod(String clazz, String method, Class<?>... argsType) {
        boolean result = false;
        try {
            Class<?> c = Class.forName(clazz);
            Method m = null;
            if(argsType == null) {
                m = c.getMethod(method);
            } else {
                m =c.getMethod(method, argsType);
            }
            result = (m != null);
        } catch (SecurityException e) {
            ExceptionUtil.handleException(e);
        } catch (NoSuchMethodException e) {
            ExceptionUtil.handleException(e);
        } catch (ClassNotFoundException e) {
            ExceptionUtil.handleException(e);
        }
        return result;
    }

    public static Object call(String clazz, String method, Class<?>[] argsType, Object[] args) {
        Object ret = null;
        
        try {
            Class<?> c = Class.forName(clazz);
            Method m = c.getMethod(method, argsType);
            m.setAccessible(true);
            ret = m.invoke(null, args);
        } catch (ClassNotFoundException e) {
            ExceptionUtil.handleException(e);
        } catch (SecurityException e) {
            ExceptionUtil.handleException(e);
        } catch (NoSuchMethodException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (InvocationTargetException e) {
            ExceptionUtil.handleException(e);
        }
        
        return ret;
    }

    public static Object call(Object obj, String method, Class<?>[] argsType, Object[] args) {
        Object ret = null;
        
        if (obj == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        
        try {
            Class<?> c = obj.getClass();
            Method m = c.getMethod(method, argsType);
            m.setAccessible(true);
            ret = m.invoke(obj, args);
        } catch (SecurityException e) {
            ExceptionUtil.handleException(e);
        } catch (NoSuchMethodException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (InvocationTargetException e) {
            ExceptionUtil.handleException(e);
        }
        
        return ret;
    }

    public static <T> T getField(Object obj, String field) {
        FieldHelper<T> helper = new FieldHelper<T>(field).obj(obj);
        try {
            return helper.get();
        } catch (NoSuchFieldException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        }
        return null;
    }

    public static <T> T setField(Object obj, String field, T newValue) {
        FieldHelper<T> helper = new FieldHelper<T>(field).obj(obj);
        T oldValue = null;
        try {
            oldValue = helper.get();
            helper.set(newValue);
        } catch (NoSuchFieldException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        }
        return oldValue;
    }

    public static <T> T getField(String clazz, String field) {
        try {
            FieldHelper<T> helper = new FieldHelper<T>(field).clazz(Class.forName(clazz));
            return helper.get();
        } catch (NoSuchFieldException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        } catch (ClassNotFoundException e) {
            ExceptionUtil.handleException(e);
        }
        return null;
    }

    public static <T> T setField(String clazz, String field, T newValue) {
        T oldValue = null;
        try {
            FieldHelper<T> helper = new FieldHelper<T>(field).clazz(Class.forName(clazz));
            oldValue = helper.get();
            helper.set(newValue);
        } catch (NoSuchFieldException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleException(e);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.handleException(e);
        } catch (ClassNotFoundException e) {
            ExceptionUtil.handleException(e);
        }
        return oldValue;
    }

    public static class FieldHelper<T> {
        private Object obj = null;
        private Class<?> clazz;
        private String fieldName;

        private boolean inited;
        private Field field;

        public FieldHelper(String fieldName) {
            this.fieldName = fieldName;
        }

        public FieldHelper<T> obj(Object obj) {
            if (obj == null) {
                throw new IllegalArgumentException("obj cannot be null");
            }
            this.obj = obj;
            return this;
        }

        public FieldHelper<T> clazz(Class<?> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("class cannot be null");
            }
            this.clazz = clazz;
            return this;
        }

        private void prepare() {
            if (inited)
                return;
            inited = true;

            if (obj != null)
                clazz = obj.getClass();
            while (clazz != null) {
                try {
                    Field f = clazz.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    field = f;
                    return;
                } catch (Exception e) {
                } finally {
                    clazz = clazz.getSuperclass();
                }
            }
        }

        public T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
            prepare();

            if (field == null)
                throw new NoSuchFieldException();

            try {
                @SuppressWarnings("unchecked")
                T r = (T) field.get(obj);
                return r;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("unable to cast object");
            }
        }

        public void set(T val) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
            prepare();

            if (field == null)
                throw new NoSuchFieldException();

            field.set(obj, val);
        }
    }

}
