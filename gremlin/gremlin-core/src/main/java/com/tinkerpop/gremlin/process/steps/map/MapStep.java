package com.tinkerpop.gremlin.process.steps.map;

import com.tinkerpop.gremlin.process.Holder;
import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.steps.AbstractStep;

import java.util.function.Function;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MapStep<S, E> extends AbstractStep<S, E> {

    public Function<Holder<S>, E> function;

    public MapStep(final Traversal traversal) {
        super(traversal);
    }

    public MapStep(final Traversal traversal, final Function<Holder<S>, E> function) {
        super(traversal);
        this.function = function;
    }

    protected Holder<E> processNextStart() {
        while (true) {
            final Holder<S> holder = this.starts.next();
            final E temp = this.function.apply(holder);
            if (NO_OBJECT != temp)
                if (holder.get().equals(temp)) // no path extension (i.e. a filter, identity, side-effect)
                    return (Holder<E>) holder.makeSibling();
                else
                    return holder.makeChild(this.getAs(), temp);
        }
    }

    public void setFunction(final Function<Holder<S>, E> function) {
        this.function = function;
    }
}

