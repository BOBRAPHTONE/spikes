package com.novoda.tpbot.socket.io;

import android.os.Handler;
import android.os.Looper;

import com.novoda.support.Observable;
import com.novoda.support.Result;
import com.novoda.tpbot.Mode;
import com.novoda.tpbot.TpService;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.novoda.tpbot.socket.io.Event.CONNECTED;
import static com.novoda.tpbot.socket.io.Event.JOIN;

public class SocketIOTpService implements TpService {

    private final Socket socket;
    private final Handler handler;

    public static SocketIOTpService getInstance() {
        return LazySingleton.INSTANCE;
    }

    private SocketIOTpService() {
        this.socket = IO.socket(URI.create("http://192.168.86.152:3000"));
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public Observable<Result> connect(Mode mode) {
        return new SocketConnectionObservable(mode);
    }

    private class SocketConnectionObservable extends Observable<Result> {

        private final Mode mode;

        SocketConnectionObservable(Mode mode) {
            this.mode = mode;
        }

        @Override
        public void start() {
            socket.on(CONNECTED.code(), new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setChanged();
                            notifyObservers(Result.from("Successfully connected"));
                        }
                    });
                }
            });
            socket.connect();
            socket.emit(JOIN.code(), mode);
        }
    }

    @Override
    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    private static class LazySingleton {
        private static final SocketIOTpService INSTANCE = new SocketIOTpService();
    }

}
