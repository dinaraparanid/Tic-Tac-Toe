package com.dinaraparanid.tictactoe.utils.polymorphism;

import androidx.annotation.NonNull;

public class State {

    @NonNull
    private final Runnable action;

    protected State(@NonNull final Runnable action) { this.action = action; }

    public final void run() { action.run(); }
}
