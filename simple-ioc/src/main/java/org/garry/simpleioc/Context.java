package org.garry.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Context
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:35
 */
public class Context implements InstanceFactory{

    private static final Logger log = LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    Environment env = null;

    public Context(String ctxName)
    {
        this.env = new Environment();
        this.ctxName = ctxName;
    }

    public Context(String ctxName, Context base)
    {
        this.env = new Environment(base.env);
        this.ctxName = ctxName;
    }

    public void addInstance(Object instance)
    {
        enrich(new InstanceDefinition(instance));
    }

    private void enrich(Definition defn) {
        log.info("Enriching {} with {}", this, defn);
        defn.checkForDependencies(this);
        env.enrich(defn);
    }

    public void defineSingleton(Class klass)
    {
        enrich(new SingletonDefinition(env,klass));
    }

    public void definePrototype(Class klass)
    {
        enrich(new PrototypeDefinition(env,klass));
    }

    @Override
    public boolean containsInstance(Class klass) {
        return env.containsInstanceDefinition(klass);
    }

    @Override
    public <T> T getInstance(Class<T> klass) {

        return getInstance(klass,true);
    }

    public <T> T getInstance(Class<T> klass, boolean required)
    {
        Definition defn = env.lookupLatestDefinition(klass);

        if(defn != null)
        {
            return (T)defn.getInstance();
        }

        if(required)
        {
            throw new RuntimeException("No definition for instance " + klass + " in " + this);
        }

        return null;
    }

    @Override
    public <T> T getOptionalInstance(Class<T> klass) {
        return getInstance(klass,false);
    }

    @Override
    public <T> T[] getInstances(Class<T> klass) {
        List<Definition> defns = env.lookupAllDefinitions(klass);

        List<T> instances = new ArrayList<>(defns.size());

        for(Definition defn: defns)
        {
            instances.add((T)defn.getInstance());
        }

        return (T[])instances.toArray(((T[]) Array.newInstance(klass,0)));
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}
