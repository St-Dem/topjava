package ru.javawebinar.topjava.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String id) {
        super("Meals " + id + " already exist", id);
    }

    public ExistStorageException(String message, Exception e) {
        super(message, e);
    }
}
