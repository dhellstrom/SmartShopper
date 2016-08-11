package com.summer.daniel.smartshopper.database;

/**
 * Created by Daniel on 2016-08-10.
 */
public class DbSchema {

    public static class StoreTable{
        public static final String NAME = "stores";

        public static final class Cols{
            public static final String STORE_NAME = "store name";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String CATEGORIES = "categories";
        }
    }

    public static final class ItemTable{
        public static final String NAME = "items";

        public static final class Cols{
            public static final String ITEM_NAME = "item name";
            public static final String CATEGORY = "category";
        }

    }

    public static final class CategoryTable{
        public static final String NAME = "categories";

        public static final class Cols{
            public static final String CATEGORY_NAME = "category name";
        }
    }

    public static final class ListTable{
        public static final String NAME = "lists";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String LIST_NAME = "list name";
            public static final String ITEMS = "items";
            public static final String PURCHASED = "purchased";
        }
    }
}
