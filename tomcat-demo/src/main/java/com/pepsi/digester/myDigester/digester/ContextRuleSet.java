package com.pepsi.digester.myDigester.digester;

import com.pepsi.digester.myDigester.javaBean.Context;
import com.pepsi.digester.myDigester.javaBean.Valve;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class ContextRuleSet implements RuleSet{

    protected final String prefix;

    public ContextRuleSet(String prefix){
        this.prefix = prefix;
    }


    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public void addRuleInstances(Digester digester) {
        digester.addObjectCreate(prefix + "Context", Context.class);
        digester.addSetProperties(prefix + "Context");
        digester.addSetNext(prefix + "Context",
                "addChild");

    }
}
