package com.pepsi.digester.myDigester.digester;

import com.pepsi.digester.myDigester.javaBean.Engine;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class EngineRuleSet implements RuleSet {

    protected final String prefix;

    public EngineRuleSet(String prefix) {
        this.prefix = prefix;
    }


    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public void addRuleInstances(Digester digester) {
        digester.addObjectCreate(prefix + "Engine", Engine.class);
        digester.addSetProperties(prefix + "Engine");
        digester.addSetNext(prefix + "Engine", "setEngine");

    }
}
