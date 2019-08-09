package com.tydic.beijing.billing.ua.dao;

import com.tydic.uda.UdaAnnotationSetKey;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Bradish7Y on 15/5/21.
 */
public class DuplicatedRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;

    @Id
    public String getKey() {
        return key;
    }
    @UdaAnnotationSetKey
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DuplicatedRecord{" +
                "key='" + key + '\'' +
                '}';
    }
}
