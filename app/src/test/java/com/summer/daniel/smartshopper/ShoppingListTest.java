package com.summer.daniel.smartshopper;

/**
 * Created by Daniel on 2016-08-12.
 */

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class ShoppingListTest {

    private ShoppingList list;
    private String name;

    @Before
    public void setUp(){
        name = "TEST LIST";
        list = new ShoppingList(name);
    }

    @Test
    public void testName(){
        assertEquals("Wrong name", name, list.getName());
    }

    @Test
    public void testAddItem() {
        ShopItem item1 = new ShopItem("Item1", "Category1");
        ShopItem item2 = new ShopItem("Item2", "Category2");

        assertTrue(list.addItem(item1));
        assertFalse(list.addItem(item1));
        assertTrue(list.addItem(item2));

        assertArrayEquals("Wrong contents", new String[]{"Item1", "Item2"}, list.getItemNames());
    }

    @Test
    public void testRemoveItem(){
        ShopItem item1 = new ShopItem("Item1", "Category1");
        ShopItem item2 = new ShopItem("Item2", "Category2");

        list.addItem(item1);
        list.addItem(item2);
        assertTrue(list.removeItem(item2.getName()));

        assertArrayEquals("Wrong contents", new String[]{"Item1"}, list.getItemNames());
    }

    @Test
    public void testTogglePurchased(){
        ShopItem item = new ShopItem("Item1", "Category1");

        list.addItem(item);
        list.toggleItemPurchased(item.getName());
        assertTrue("Item not toggled", list.getItemPurchasedStatus(item.getName()));
    }


}
