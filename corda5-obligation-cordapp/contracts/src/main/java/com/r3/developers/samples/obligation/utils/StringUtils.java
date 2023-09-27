package com.r3.developers.samples.obligation.utils;

import org.jetbrains.annotations.Nullable;

public class StringUtils {
    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }
}
