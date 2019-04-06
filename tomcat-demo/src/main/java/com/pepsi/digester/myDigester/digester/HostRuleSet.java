package com.pepsi.digester.myDigester.digester;

import com.pepsi.digester.myDigester.javaBean.Host;
import com.pepsi.digester.myDigester.javaBean.Valve;
import org.apache.catalina.startup.CopyParentClassLoaderRule;
import org.apache.catalina.startup.LifecycleListenerRule;
import org.apache.catalina.startup.RealmRuleSet;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class HostRuleSet implements RuleSet {

    protected final String prefix;

    public HostRuleSet(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public void addRuleInstances(Digester digester) {

        digester.addObjectCreate(prefix + "Host", Host.class);
        digester.addSetProperties(prefix + "Host");
        digester.addSetNext(prefix + "Host", "addChild");

        digester.addObjectCreate(prefix + "Host/Valve", Valve.class);
        digester.addSetProperties(prefix + "Host/Valve");
        digester.addSetNext(prefix + "Host/Valve",
                "addValve",
                "com.pepsi.digester.myDigester.javaBean.Valve");
    }
}
