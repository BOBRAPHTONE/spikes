package com.novoda.tpbot;

import com.novoda.support.Observable;
import com.novoda.support.Result;

public interface TpService {

    Observable<Result> connect(String username);

    Observable<Result> disconnect();

}
