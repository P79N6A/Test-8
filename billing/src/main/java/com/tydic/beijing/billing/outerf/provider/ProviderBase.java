/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.provider;

import com.tydic.beijing.billing.outerf.architecture.Disposable;
import com.tydic.beijing.billing.outerf.architecture.LogAble;

/**
 *
 * @author wangshida
 */
public abstract class ProviderBase extends LogAble {
    protected Disposable busi;

    public ProviderBase() {
    }

    public ProviderBase(Disposable busi) {
        this.busi = busi;
    }
    
    public void setBusi(Disposable busi) {
        this.busi = busi;
    }
}
