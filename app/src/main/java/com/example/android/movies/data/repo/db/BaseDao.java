package com.example.android.movies.data.repo.db;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

// https://gist.github.com/florina-muntenescu/1c78858f286d196d545c038a71a3e864
public abstract class BaseDao<T> {
    public void upsertItems(List<T> items) {
        Long[] insertResult = insert(items);
        List<T> updateList2 = new ArrayList<>();
        for (int i = 0; i < insertResult.length; i++) {
            if (insertResult[i] == -1L) {
                updateList2.add(items.get(i));
            }
        }
        update(updateList2);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Long insert(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Long[] insert(T[] obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Long[] insert(List<T> obj);

    @Update
    public abstract void update(List<T> obj);

    @Delete
    public abstract void delete(T obj);
}