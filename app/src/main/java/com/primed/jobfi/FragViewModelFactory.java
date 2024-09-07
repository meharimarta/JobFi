package com.primed.jobfi;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FragViewModelFactory implements ViewModelProvider.Factory
 {
    private User user;
    private Context context;
    private Fragment host;
    public FragViewModelFactory(User user, Context ctx, Fragment f) {
        this.user = user;
        this.context = ctx;
        this.host = f;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragViewModel.class)) {
            return (T) new FragViewModel(user, context, host);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
