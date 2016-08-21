package com.summer.daniel.smartshopper.database;

/**
 * Created by Daniel on 2016-08-10.
 * Contains table and column names.
 */
public class DbSchema {

    public static class StoreTable{
        public static final String NAME = "stores";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String STORE_NAME = "storeName";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String CATEGORIES = "categories";
        }
    }

    public static final class ItemTable{
        public static final String NAME = "items";

        public static final class Cols{
            public static final String ITEM_NAME = "itemName";
            public static final String CATEGORY = "category";
        }

    }

    public static final class CategoryTable{
        public static final String NAME = "categories";

        public static final class Cols{
            public static final String CATEGORY_NAME = "categoryName";
        }
    }

    public static final class ListTable{
        public static final String NAME = "lists";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String LIST_NAME = "listName";
            public static final String ITEMS = "items";
            public static final String PURCHASED = "purchased";
        }
    }
}
