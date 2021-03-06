package com.dinaraparanid.tictactoe.viewmodels;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;

import com.dinaraparanid.tictactoe.MainActivity;
import com.dinaraparanid.tictactoe.R;
import com.dinaraparanid.tictactoe.fragments.SelectGameTypeFragment;

public final class MainActivityViewModel extends BaseObservable {

    @NonNull
    private final MainActivity activity;

    public MainActivityViewModel(@NonNull final MainActivity activity) { this.activity = activity; }

    public final void showSelectGameTypeFragment() {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        SelectGameTypeFragment.newInstance()
                )
                .addToBackStack(null)
                .commit();
    }
}
