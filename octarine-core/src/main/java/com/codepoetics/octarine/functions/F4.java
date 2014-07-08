package com.codepoetics.octarine.functions;

import com.codepoetics.octarine.tuples.T4;

import java.util.function.Function;

public interface F4<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);

    default F3<B, C, D, R> curry(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default F2<C, D, R> curry(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default F1<D, R> curry(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }

    default F0<R> curry(A a, B b, C c, D d) {
        return () -> apply(a, b, c, d);
    }

    default <R2> F4<A, B, C, D, R2> andThen(Function<R, R2> f) {
        return (a, b, c, d) -> f.apply(apply(a, b, c, d));
    }

    default <S> Function<S, R> compose(Function<S, T4<A, B, C, D>> f) {
        return s -> f.apply(s).sendTo(this);
    }
}