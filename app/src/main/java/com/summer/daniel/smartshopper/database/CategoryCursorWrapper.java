package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.summer.daniel.smartshopper.database.DbSchema.CategoryTable;

/**
 * Created by Daniel on 2016-08-11.
 */
public class CategoryCursorWrapper extends CursorWrapper {
    public CategoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String getCategory(){
        String category = getString(getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));

        return category;
    }

}
