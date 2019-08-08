package com.huishuo.hdata.entity;

import com.huishuo.hdata.entity.tuple.Tuple2;

import java.util.Optional;

public abstract class Tuple {

    public abstract <A> Optional<A> _1();

    public abstract <B> Optional<B> _2();

    public abstract <C> Optional<C> _3();

    public static <A, B> Tuple of (A a, B b){
        return new Tuple2(a, b);
    }
}
