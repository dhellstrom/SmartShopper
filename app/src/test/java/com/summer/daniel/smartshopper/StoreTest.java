package com.summer.daniel.smartshopper;

/**
 * Created by Daniel on 2016-08-10.
 */

import com.google.android.gms.maps.model.LatLng;
import com.summer.daniel.smartshopper.model.Store;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest {

    private Store store;

    @Before
    public void setUp(){
        store = new Store("StoreName", new LatLng(11.11, 22.22));
    }

    @Test
    public void testAddCategories(){
        int noCategories = 13;
        for(int i = 0; i < noCategories; i++){
            store.addCategory("category" + i);
        }

        assertEquals("Wrong size ", noCategories, store.getNumberOfCategories());
    }

    @Test
    public void testIncreasePriority(){
        store.addCategory("category0");
        store.addCategory("category1");
        store.addCategory("category2");
        store.increasePriority("category1");

        String[] expected = {"category1", "category0", "category2"};

        assertArrayEquals("Wrong order", expected, store.getCategories());
    }

    @Test
    public void testDecreasePriority(){
        store.addCategory("category0");
        store.addCategory("category1");
        store.addCategory("category2");
        store.decreasePriority("category1");

        String[] expected = {"category0", "category2", "category1"};

        assertArrayEquals("Wrong order", expected, store.getCategories());
    }
}
