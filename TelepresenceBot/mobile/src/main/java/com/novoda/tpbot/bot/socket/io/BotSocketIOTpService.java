package com.novoda.tpbot.bot.socket.io;

import android.os.Handler;
import android.os.Looper;

import com.novoda.support.Observable;
import com.novoda.support.Result;
import com.novoda.tpbot.human.socket.io.Move;
import com.novoda.tpbot.bot.BotTpService;

import java.net.URI;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BotSocketIOTpService implements BotTpService {

    private final Socket socket;
    private final Handler handler;

    public static BotSocketIOTpService getInstance() {
        return LazySingleton.INSTANCE;
    }

    private BotSocketIOTpService() {
        this.socket = IO.socket(URI.create("http://192.168.86.152:3000"));
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public Observable<Result> connect() {
        return new SocketConnectionObservable();
    }

    private class SocketConnectionObservable extends Observable<Result> {

        @Override
        public void start() {
            if (socket.connected()) {
                setChanged();
                notifyObservers(Result.from("Already connected"));
                return;
            }

            socket.connect();
            socket.emit("join_as_bot", "", new Ack() {
                @Override
                public void call(Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setChanged();
                            notifyObservers(Result.from("Connected"));
                        }
                    });
                }
            });
        }

    }

    @Override
    public Observable<Move> listen() {
        return new SocketMoveObservable();
    }

    private class SocketMoveObservable extends Observable<Move> {
        @Override
        public void start() {
            socket.on("move", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (args[0] != null) {
                                Object object = args[0];
                                Move move = Move.from(object.toString());

                                setChanged();
                                notifyObservers(move);
                            }

                        }
                    });
                }
            });
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
        private static final BotSocketIOTpService INSTANCE = new BotSocketIOTpService();
    }

}
