package com.antyzero.rxtraps;

import org.junit.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

public class ApiTest {

    @Test
    public void testValid() throws Exception {

        // given
        Api api = Mockito.mock(Api.class);
        String testString = "TestString";
        Mockito.when(api.getData()).thenReturn(Observable.just(testString));
        TestSubscriber<String> testSubscriber = new TestSubscriber<String>();

        // when
        api.getData().subscribe(testSubscriber);

        // then
        testSubscriber.assertValue(testString);
    }

    @Test
    public void testValidWithError() throws Exception {

        // given
        Api api = Mockito.mock(Api.class);
        Mockito.when(api.getData()).thenReturn(Observable.<String>error(new IllegalStateException("Unable to obtain data")));
        TestSubscriber<String> testSubscriber = new TestSubscriber<String>();

        // when
        api.getData().subscribe(testSubscriber);

        // then
        testSubscriber.assertError(IllegalStateException.class);
    }

    /**
     * Even if we do have 'doOnError' it won't save us from crash
     *
     * @throws Exception
     */
    @Test//(expected = OnErrorNotImplementedException.class)
    public void testValidWithError2() throws Exception {

        // given
        Api api = Mockito.mock(Api.class);
        Mockito.when(api.getData()).thenReturn(Observable.<String>error(new IllegalStateException("Unable to obtain data")));

        // when
        api.getData()
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("Next: " + s);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("We got error " + throwable);
                    }
                })
                .subscribe();

        // then
        // nothing
    }

    /**
     * This on the other hand will work just fine
     *
     * @throws Exception
     */
    @Test
    public void testValidWithError3() throws Exception {

        // given
        Api api = Mockito.mock(Api.class);
        Mockito.when(api.getData()).thenReturn(Observable.<String>error(new IllegalStateException("Unable to obtain data")));

        // when
        api.getData()
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println("Next: " + s);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                System.out.println("We got error " + throwable);
                            }
                        }

                );

        // then
        // nothing
    }
}