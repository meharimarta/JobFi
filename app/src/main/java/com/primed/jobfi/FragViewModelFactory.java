package com.primed.jobfi;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

public class FragViewModelFactory implements ViewModelProvider.Factory
 {
    private User user;
    private Context context;
    public FragViewModelFactory(User user, Context ctx) {
        this.user = user;
        this.context = ctx;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragViewModel.class)) {
            return (T) new FragViewModel(user, context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
