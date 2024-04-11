/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.advantech.converter.Encodeable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public enum BabStatus implements Encodeable {
    CLOSED(1),
    PAUSE(2),
    AUTO_CLOSED(3),
    NO_RECORD(-1),
    UNFINSHED(-2),
    UNFINSHED_RECONNECTABLE(-3);

    private final Integer value;
    private static final Map map = new HashMap<>();

    private BabStatus(Integer value) {
        this.value = value;
    }

    static {
        for (BabStatus b : BabStatus.values()) {
            map.put(b.value, b);
        }
    }

    public static BabStatus valueOf(int pageType) {
        return (BabStatus) map.get(pageType);
    }

    @Override
    public Integer token() {
        return this.value;
    }
}
