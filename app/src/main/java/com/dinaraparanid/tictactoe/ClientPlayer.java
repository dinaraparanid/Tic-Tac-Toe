package com.dinaraparanid.tictactoe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dinaraparanid.tictactoe.utils.polymorphism.Player;
import com.dinaraparanid.tictactoe.utils.polymorphism.State;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ClientPlayer extends Player {

    @NonNls
    @NonNull
    private static final String TAG = "ClientPlayer";

    public static final Parcelable.Creator<ClientPlayer> CREATOR = new Creator<ClientPlayer>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public final ClientPlayer createFromParcel(@NonNull final Parcel source) {
            return new ClientPlayer(
                    source.readByte(),
                    source.readByte(),
                    source.readString()
            );
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public ClientPlayer[] newArray(final int size) { return new ClientPlayer[0]; }
    };

    @NonNull
    private String hostName;

    @NonNull
    SocketChannel client;

    @NonNull
    private final State[] states = {
            new ShowRoleState(),
            new CorrectMoveState(),
            new InvalidMoveState(),
            new GameFinishedState()
    };

    protected final ExecutorService executor = Executors.newCachedThreadPool();

    public ClientPlayer() {
        number = 2;

        new InputDialog.Builder()
                .setMessage(R.string.host_ip)
                .setOkAction(param -> {
                    hostName = param;

                    try {
                        establishConnection();
                    } catch (final ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    return null;
                })
                .build()
                .show(Player.ApplicationAccessor.activity.getSupportFragmentManager(), null);
    }

    public ClientPlayer(final byte role, final byte turn, @NonNull final String hostName) {
        number = 2;
        this.role = role;
        this.turn = turn;
        this.hostName = hostName;

        try {
            establishConnection();
        } catch (final ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final class ShowRoleState extends State {
        ShowRoleState() {
            super(new Runnable() {
                @Override
                public final void run() {
                    Log.d(TAG, "Show role");

                    try {
                        setRole();
                    } catch (final ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    Player.ApplicationAccessor.activity.runOnUiThread(
                            () -> showRole(Player.ApplicationAccessor.activity)
                    );

                    startGame();
                }
            });
        }
    }

    private final class CorrectMoveState extends State {
        CorrectMoveState() {
            super(new Runnable() {
                @Override
                public final void run() {
                    Log.d(TAG, "Correct move");
                    updateTurn();
                    gameFragment.get().updateTable(readTable());
                }
            });
        }
    }

    private final class InvalidMoveState extends State {
        InvalidMoveState() {
            super(new Runnable() {
                @Override
                public final void run() {
                    Log.d(TAG, "Invalid move");
                    gameFragment.get().showInvalidMove();
                }
            });
        }
    }

    private final class GameFinishedState extends State {
        GameFinishedState() {
            super(new Runnable() {
                @Override
                public final void run() {
                    Log.d(TAG, "Game Finished");
                    isPlaying.set(false);
                    gameFragment.get().gameFinished();
                }
            });
        }
    }

    @Override
    public final void sendReady() {
        Log.d(TAG, "Send ready");

        try {
            executor.submit(() -> {
                final ByteBuffer sayHelloBuffer = ByteBuffer.allocate(1);
                sayHelloBuffer.put(Server.PLAYER_IS_FOUND);
                sayHelloBuffer.flip();

                try { client.write(sayHelloBuffer); }
                catch (final IOException e) { e.printStackTrace(); }
                catch (final NullPointerException e) {
                    // TODO: No service running
                }
            }).get();
        } catch (final ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void sendMove(final int y, final int x) {
        Log.d(TAG, "Send move");

        try {
            executor.submit(() -> {
                final ByteBuffer sendMoveBuffer = ByteBuffer.allocate(3);
                sendMoveBuffer.put(Server.PLAYER_MOVED);
                sendMoveBuffer.put((byte) y);
                sendMoveBuffer.put((byte) x);
                sendMoveBuffer.flip();

                try { client.write(sendMoveBuffer); }
                catch (final IOException e) { e.printStackTrace(); }
            }).get();
        } catch (final ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NonNls
    @NonNull
    @Override
    public final String toString() {
        return "ClientPlayer{" +
                "client=" + client +
                ", states=" + Arrays.toString(states) +
                '}';
    }

    protected final void startCycle() {
        byte command = -1;

        try {
            while (command != Server.COMMAND_GAME_FINISH) {
                final ByteBuffer readBuffer = ByteBuffer.allocate(1);

                if (client.read(readBuffer) < 0)
                    continue;

                readBuffer.flip();
                command = readBuffer.get();

                Log.d(TAG + " COMMAND", Integer.toString(command));

                states[command].run();
            }

            client.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private final void establishConnection() throws ExecutionException, InterruptedException {
        executor.submit(() -> {
            try {
                client = SocketChannel.open();
                client.connect(new InetSocketAddress(hostName, Server.PORT));
                new Thread(this::startCycle).start();
                sendReady();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final UnresolvedAddressException e) {
                Toast.makeText(
                        Player.ApplicationAccessor.activity,
                        R.string.invalid_ip,
                        Toast.LENGTH_LONG
                ).show();
            }
        }).get();
    }

    protected final void setRole() throws ExecutionException, InterruptedException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                final ByteBuffer readBuffer = ByteBuffer.allocate(1);

                try { client.read(readBuffer); }
                catch (final IOException e) { e.printStackTrace(); }

                readBuffer.flip();
                role = readBuffer.get();
            }
        }).get();
    }

    @NonNull
    protected final byte[][] readTable() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);

        try { client.read(buffer); }
        catch (final IOException e) { e.printStackTrace(); }

        buffer.flip();

        final byte[][] gameTable = new byte[Server.gameTableSize][Server.gameTableSize];

        for (int i = 0; i < Server.gameTableSize; i++)
            for (int q = 0; q < Server.gameTableSize; q++)
                gameTable[i][q] = buffer.get();

        return gameTable;
    }
}
