/**
 *
 */
package com.slimgears.rxrpc.apt.util;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static <T, R extends T> Stream<R> ofType(Class<R> clazz, Stream<T> source) {
        return source.filter(clazz::isInstance).map(clazz::cast);
    }

    public static <T, R extends T> Function<T, Stream<R>> ofType(Class<R> clazz) {
        return item -> ofType(clazz, Stream.of(item));
    }

    public static <T> Function<T, T> self() {
        return s -> s;
    }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }

    private static <T>Spliterator<T> takeWhile(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        return new Spliterators.AbstractSpliterator<T>(spliterator.estimateSize(), 0) {
            private boolean finished = false;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return spliterator.tryAdvance(item -> {
                    if (predicate.test(item)) {
                        action.accept(item);
                    } else {
                        finished = true;
                    }
                }) && !finished;
            }
        };
    }
}