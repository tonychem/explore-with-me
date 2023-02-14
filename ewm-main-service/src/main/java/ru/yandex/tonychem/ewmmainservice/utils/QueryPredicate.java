package ru.yandex.tonychem.ewmmainservice.utils;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryPredicate {
    private final List<Predicate> predicates = new ArrayList<>();

    public static QueryPredicate newQueryPredicate() {
        return new QueryPredicate();
    }

    public <T> QueryPredicate add(T obj, Function<T, Predicate> mapper) {
        if (obj != null) {
            predicates.add(mapper.apply(obj));
        }
        return this;
    }

    public Predicate allOf() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate anyOf() {
        return ExpressionUtils.anyOf(predicates);
    }
}
