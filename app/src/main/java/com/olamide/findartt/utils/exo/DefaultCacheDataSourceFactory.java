package com.olamide.findartt.utils.exo;


import androidx.annotation.NonNull;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;

import javax.inject.Inject;

/**
 * A {@link DataSource.Factory} that implements cache mechanism
 *
 * @author nuhkoca
 */
public class DefaultCacheDataSourceFactory implements DataSource.Factory {

    private CacheDataSource cacheDataSource;

    /**
     * A default constructor that initializes cache mechanism
     *
     * @param cacheDataSource represents an instance of {@link CacheDataSource}
     */
    @Inject
    public DefaultCacheDataSourceFactory(@NonNull CacheDataSource cacheDataSource) {
        super();
        this.cacheDataSource = cacheDataSource;
    }

    /**
     * Creates the data source
     *
     * @return an instance of {@link DataSource}
     */
    @Override
    public DataSource createDataSource() {
        return cacheDataSource;
    }
}
