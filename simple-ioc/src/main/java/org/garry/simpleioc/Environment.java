package org.garry.simpleioc;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Environment
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:23
 */
public class Environment {

    private static final class Binding
    {
        private Definition defn;

        private Binding prev;
    }

    Binding top = null;

    Environment()
    {

    }

    Environment(Environment original)
    {
        this.top = original.top;
    }

    void enrich(Definition defn)
    {
        Binding oldTop = top;

        top = new Binding();

        top.defn = defn;
        top.prev = oldTop;
    }

    Definition lookupLatestDefinition(Class klass)
    {
        for(Binding pos = top; pos != null; pos = pos.prev)
        {
            if (klass.isAssignableFrom(pos.defn.getDefinitionClass()))
            {
                return pos.defn;
            }
        }
        return null;
    }

    List<Definition> lookupAllDefinitions(Class klass)
    {
        LinkedList<Definition> defns = new LinkedList<>();

        for(Binding pos = top; pos != null; pos = pos.prev)
        {
            if(klass.isAssignableFrom(pos.defn.getDefinitionClass()))
            {
                defns.addFirst(pos.defn);
            }
        }
        return defns;
    }

    boolean containsInstanceDefinition(Class klass)
    {
        return (lookupLatestDefinition(klass) != null);
    }
}
