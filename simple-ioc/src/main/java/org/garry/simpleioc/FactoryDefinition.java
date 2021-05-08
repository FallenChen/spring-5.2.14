package org.garry.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @ClassName FactoryDefinition
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:51
 */
abstract class FactoryDefinition extends Definition{

    private static final Logger log =
            LoggerFactory.getLogger(FactoryDefinition.class);

    Environment env;
    Class klass;
    Constructor ctor;

    void invalid(String reason)
    {
        log.error("Invalid definition: {}, {}",this,reason);

        throw new RuntimeException("Invalid Definition of " + klass + ": " + reason);
    }

    FactoryDefinition(Environment env, Class klass)
    {
        this.env = env;
        this.klass = klass;

        if(klass.isInterface())
        {
            invalid("Interfaces cannot be instantiated.");
        }
        if(Modifier.isAbstract(klass.getModifiers()))
        {
            invalid("Abstract classes cannot be instantiated.");
        }
        this.ctor = findInjectionConstructor();
    }

    boolean isExplicitInjectionConstructor(Constructor ctor)
    {
        return (ctor.getAnnotation(Inject.class) != null);
    }

    Constructor findExplicitInjectionConstructor()
    {
        Constructor ctor = null;

        for(Constructor c : klass.getDeclaredConstructors()) {
            if (!isExplicitInjectionConstructor(c)) {
                log.debug("{} is not injection constructor", c);
                continue;
            }

            if (ctor != null) {
                invalid("Cannot have multiple explicit injection constructors");
            }
            ctor = c;
        }
        return ctor;
    }

    Constructor findImplicitInjectionConstructor()
    {
        Constructor ctors[] = klass.getDeclaredConstructors();

        if(ctors.length != 1)
        {
            invalid(klass + "Must have one public constructor for implicit injection");
        }
        return ctors[0];
    }

    Constructor findInjectionConstructor()
    {
        Constructor ctor = findExplicitInjectionConstructor();

        if (ctor == null)
        {
            ctor = findImplicitInjectionConstructor();
        }
        if(ctor == null)
        {
            invalid("No constructor available for injection .");
        }
        log.debug("{} has injection constructor: {}",this, ctor);

        return ctor;
    }

    void checkForDependencies(InstanceFactory factory)
    {
        for (Class dep: ctor.getParameterTypes())
        {
            if(factory.containsInstance(dep))
            {
                continue;
            }
            log.error("Missing dependency {} in {}",dep,this);

            throw new RuntimeException("Unknown dependency: " + dep);
        }
    }

    Object[] getConstructorArguments()
    {
        Class[] ctorParameterTypes = ctor.getParameterTypes();

        Object[] ctorArguments = new Object[ctorParameterTypes.length];

        for(int argNum = 0; argNum < ctorArguments.length; argNum++)
        {
            Definition defn = env.lookupLatestDefinition(ctorParameterTypes[argNum]);
            ctorArguments[argNum] = defn.getInstance();
        }
        return ctorArguments;
    }

    <T> T constructInstance(Class<T> klass)
    {
        log.debug("{}, constructing instance", this);

        Object[] ctorArguments = getConstructorArguments();

        T object = null;

        try {
            object = (T)ctor.newInstance(ctorArguments);
        }catch (Exception ex)
        {
            throw new RuntimeException("Error instantiating object.",ex);
        }
        log.debug("{}, constructed instance := {}",this, object);
        return object;
    }

    @Override
    Class getDefinitionClass() {
        return klass;
    }
}
