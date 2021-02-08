package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.exception.ExistStorageException;
import ru.javawebinar.topjava.exception.NotExistStorageException;
import ru.javawebinar.topjava.model.Meal;
import java.util.Collections;
import java.util.List;


public abstract class AbstractStorage<T> implements Storage {


    protected abstract T getSearchKey(String id);

    protected abstract void doUpdate(Meal meal, T searchKey);

    protected abstract boolean isExist(T searchKey);

    protected abstract void doSave(Meal meal, T searchKey);

    protected abstract Meal doGet(T searchKey);

    protected abstract void doDelete(T searchKey);

    protected abstract List<Meal> doCopyAll();

    public void update(Meal meal) {
        T searchKey = getExistedSearchKey(meal.getId());
        doUpdate(meal, searchKey);
    }

    public void save(Meal meal) {
        T searchKey = getNotExistedSearchKey(meal.getId());
        doSave(meal, searchKey);
    }

    public void delete(String id) {
        T searchKey = getExistedSearchKey(id);
        doDelete(searchKey);
    }

    public Meal get(String id) {
        T searchKey = getExistedSearchKey(id);
        return doGet(searchKey);
    }

    private T getExistedSearchKey(String id) {
        T searchKey = getSearchKey(id);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(id);
        }
        return searchKey;
    }

    private T getNotExistedSearchKey(String id) {
        T searchKey = getSearchKey(id);
        if (isExist(searchKey)) {
            throw new ExistStorageException(id);
        }
        return searchKey;
    }


    public List<Meal> getAllSorted() {
        List<Meal> list = doCopyAll();
        Collections.sort(list);
        return list;
    }
}
