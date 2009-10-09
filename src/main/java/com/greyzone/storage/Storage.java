package com.greyzone.storage;

import java.util.List;

public interface Storage<T> {

    public List<T> getItems();

    public void storeItems(List<T> items);

    public void testParsing();
}
