package ru.javawebinar.topjava.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String id) {
        super("Meals " + id + " not exist", id);
    }
}
