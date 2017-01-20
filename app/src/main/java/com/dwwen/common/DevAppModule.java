package com.dwwen.common;

import com.dwwen.settings.DevSettings;
import com.dwwen.settings.Settings;

import dagger.Module;
import dagger.Provides;

@Module(includes = ProdAppModule.class, overrides = true, library = true)
public class DevAppModule {

    @Provides
    Settings provideSettings(DevSettings settings) {
        return settings;
    }
}
