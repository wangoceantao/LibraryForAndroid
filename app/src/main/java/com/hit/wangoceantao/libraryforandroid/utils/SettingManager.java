package com.hit.wangoceantao.libraryforandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @Description: sharepreference
 * <p/>
 * Created by wanghaitao on 16/6/15 17:27.
 * <p/>
 * Email：wangoceantao@gmail.com
 */
public class SettingManager {
    /**
     * Keys for Preference
     */
    public static class KEYS {
        /**TODO */
        public static final String LOCAL_CONVERSATIONS = "local_conversations";
    }

    /**
     * Name of default preference file
     */
    private static final String ROAM_PREFERENCE = "roam_preference";

    /**
     * 通用Preference值存储
     *
     * @param context !NotNull
     * @param key     the special key
     * @param value   support:{@link Integer},{@link String},{@link Boolean},
     *                {@link Long},{@link Float}
     * @param <T>     the special value type
     */
    public static <T> void save(Context context, String key, T value) {
        save(context, key, value, ROAM_PREFERENCE);
    }

    /**
     * 通用Preference值获取
     *
     * @param context  !NotNull
     * @param key      the special key
     * @param defValue !NotNull, default value
     * @param <T>      the special value type
     * @return if defValue|context|key is null, return null
     */
    public static <T> T getValue(Context context, String key, T defValue) {
        return getValue(context, key, defValue, ROAM_PREFERENCE);
    }

    /**
     * 通用Preference值存储
     *
     * @param context  !NotNull
     * @param key      the special key
     * @param value    support:{@link Integer},{@link String},{@link Boolean},
     *                 {@link Long},{@link Float}
     * @param fileName name of preference file. if null, save to TVGAME_PREFERENCE
     * @param <T>      the special value type
     */
    public static <T> void save(Context context, String key, T value, String fileName) {
        if (TextUtils.isEmpty(key) || context == null) {
            return;
        }

        SharedPreferences sp = context
                .getSharedPreferences(TextUtils.isEmpty(fileName) ? ROAM_PREFERENCE : fileName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).commit();
        } else if (value instanceof String) {
            editor.putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).commit();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value).commit();
        }
    }

    /**
     * 通用Preference值获取
     *
     * @param context  !NotNull
     * @param key      the special key
     * @param defValue !NotNull, default value
     * @param fileName name of preference file. if null, get from ROAM_PREFERENCE
     * @param <T>      the special value type
     * @return if defValue|context|key is null, return null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Context context, String key, T defValue, String fileName) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences sp = context
                .getSharedPreferences(TextUtils.isEmpty(fileName) ? ROAM_PREFERENCE : fileName, Context.MODE_MULTI_PROCESS);
        Object res = null;

        if (defValue instanceof Integer) {
            res = sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            res = sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof String) {
            res = sp.getString(key, (String) defValue);
        } else if (defValue instanceof Float) {
            res = sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            res = sp.getLong(key, (Long) defValue);
        }

        return (res == null) ? null : (T) res;
    }
}
