package com.gt.copa.infra;

import lombok.Getter;

public enum SiNo {
    SI(true),
    NO(false);

    @Getter
    boolean value;

    private SiNo(boolean value) {
        this.value = value;
    }

    public static SiNo byValue(boolean value) {
        if(value) {
            return SI;
        }
        return NO;
    }
}
