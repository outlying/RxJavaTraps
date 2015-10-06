package com.antyzero.rxtraps;

import rx.Observable;

public interface Api {

    Observable<String> getData();
}
