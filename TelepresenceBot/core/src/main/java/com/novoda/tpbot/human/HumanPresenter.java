package com.novoda.tpbot.human;

import com.novoda.tpbot.Direction;
import com.novoda.tpbot.Result;
import com.novoda.tpbot.support.Observable;
import com.novoda.tpbot.support.Observer;

import static com.novoda.tpbot.support.Observable.unsubscribe;

class HumanPresenter {

    private final HumanTpService humanTpService;
    private final HumanView humanView;

    private Observable<Result> observable;

    HumanPresenter(HumanTpService humanTpService, HumanView humanView) {
        this.humanTpService = humanTpService;
        this.humanView = humanView;
    }

    void startPresenting(String serverAddress) {
        observable = humanTpService.connectTo(serverAddress)
                .attach(new ConnectionObserver())
                .start();
    }

    void stopPresenting() {
        unsubscribe(observable);
        humanTpService.disconnect();
    }

    void sendDirection(Direction direction) {
        humanTpService.moveIn(direction);
    }

    private class ConnectionObserver implements Observer<Result> {

        @Override
        public void update(Result result) {
            if (result.isError()) {
                humanView.onError(result.exception().get().getMessage());
            } else {
                humanView.onConnect(result.message().get());
            }
        }

    }

}
