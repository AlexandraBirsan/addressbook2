// --------------------------------------------------------------------------
// Copyright © 2010, SPM Software LP. All Rights Reserved.
// This program belongs to SPM Software LP.  It is considered a TRADE SECRET and
// is not to be divulged or used by parties who have not received written
// authorization from SPM Software LP.
// --------------------------------------------------------------------------
package com.addressbook;

import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Adrian Miron (miron@synygy.com)
 */
public class EasyMockNiceCreator<T> implements FactoryBean<T> {

    private Class<T> iface;

    @Override
    public T getObject() throws Exception {
        T mock = EasyMock.createNiceMock(iface);
        EasyMock.replay(mock);
        return mock;
    }

    @Override
    public Class<?> getObjectType() {
        return iface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setIface(Class<T> iface) {
        this.iface = iface;
    }
}