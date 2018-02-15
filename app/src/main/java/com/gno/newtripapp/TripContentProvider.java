   package com.gno.newtripapp;


   import android.content.ContentProvider;
   import android.content.ContentUris;
   import android.content.ContentValues;
   import android.content.Context;
   import android.content.UriMatcher;
   import android.database.Cursor;
   import android.database.sqlite.SQLiteDatabase;
   import android.database.sqlite.SQLiteDatabase.CursorFactory;
   import android.database.sqlite.SQLiteOpenHelper;
   import android.database.sqlite.SQLiteQueryBuilder;
   import android.net.Uri;
   import android.text.TextUtils;
   import android.util.Log;

   public class TripContentProvider extends ContentProvider {


       private static final String AUTHORITY = "com.tripcontentprovider";


       // ------- define some Uris
       private static final String PATH_EVENTITEMS = "eventitems";
       private static final String PATH_TRIPS = "trips";

       public static final Uri CONTENT_URI_EVENTITEMS = Uri.parse("content://" + AUTHORITY
               + "/" + PATH_EVENTITEMS);
       public static final Uri CONTENT_URI_TRIPS = Uri.parse("content://" + AUTHORITY
                   + "/" + PATH_TRIPS);

       // ------- maybe also define CONTENT_TYPE for each

       public static final String KEY_ID = "_id";
       public static final String KEY_TRIP_NAME = "trip_name";
       public static final String KEY_NUM_OF_DAYS = "num_of_days";
       public static final String KEY_EVENT = "event";
       public static final String KEY_TIME_FROM = "time_from";
       public static final String KEY_TIME_TO = "time_to";
       public static final String KEY_DAY = "day";
       public static final String KEY_CREATED = "created_time";


       private MySQLiteOpenHelper myOpenHelper;

       @Override
       public boolean onCreate(){
          // Construct the underlying database.
          // Defer opening the database until you need to perform
          // a query or transaction.
          myOpenHelper = new MySQLiteOpenHelper(getContext(),
              MySQLiteOpenHelper.DATABASE_NAME, null,
              MySQLiteOpenHelper.DATABASE_VERSION);

          return true;
       }

       private static final int EVENTITEMS = 1;
       private static final int EVENTITEMS_ID = 2;
       private static final int TRIPS = 3;
       private static final int TRIPS_ID = 4;

       private static final UriMatcher uriMatcher;
       //Populate the UriMatcher object, where a URI ending in 'todoitems' will
       //correspond to a request for all items, and 'todoitems/[rowID]'
       //represents a single row.
       static {
              uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
              uriMatcher.addURI(AUTHORITY, PATH_EVENTITEMS, EVENTITEMS);
              uriMatcher.addURI(AUTHORITY, PATH_EVENTITEMS + "/#", EVENTITEMS_ID);
              uriMatcher.addURI(AUTHORITY, PATH_TRIPS, TRIPS);
              uriMatcher.addURI(AUTHORITY,  PATH_TRIPS + "/#", TRIPS_ID);
       }

       @Override
       public String getType(Uri uri) {
        // Return a string that identifies the MIME type
        // for a Content Provider URI
        switch (uriMatcher.match(uri)) {

           case EVENTITEMS: return "vnd.android.cursor.dir/vnd.company.elemental1";
           case EVENTITEMS_ID: return "vnd.android.cursor.item/vnd.company.elemental1";
           case TRIPS: return "vnd.android.cursor.dir/vnd.company.elemental2";
           case TRIPS_ID: return "vnd.android.cursor.item/vnd.company.elemental2";


           default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
       }

       @Override
       public Cursor query(Uri uri, String[] projection, String selection,
                           String[] selectionArgs, String sortOrder){
         // Open a read-only database.
         SQLiteDatabase db = myOpenHelper.getWritableDatabase();

         // Replace these with valid SQL statements if necessary.
         String groupBy = null;
         String having = null;

         SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

         switch (uriMatcher.match(uri)) {

             case EVENTITEMS_ID:
             // Adding the ID to the original query
             queryBuilder.appendWhere(KEY_ID + "="
                     + uri.getLastPathSegment());
             //$FALL-THROUGH$
             case EVENTITEMS:
             queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_EVENTS_TABLE);
             break;

             case TRIPS_ID:
             // Adding the ID to the original query
             queryBuilder.appendWhere(KEY_ID + "="
                     + uri.getLastPathSegment());
             //$FALL-THROUGH$
             case TRIPS:
             queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TRIPS_TABLE);
             break;

             default:
             throw new IllegalArgumentException("Unknown URI: " + uri);
         }


         Cursor cursor = queryBuilder.query(db, projection, selection,
             selectionArgs, groupBy, having, sortOrder);

         return cursor;
       }

       @Override
       public Uri insert(Uri uri, ContentValues values)	{

         // Open a read / write database to support the transaction.
         SQLiteDatabase db = myOpenHelper.getWritableDatabase();

         // To add empty rows to your database by passing in an empty Content Values
         // object, you must use the null column hack parameter to specify the name of
         // the column that can be set to null.
         String nullColumnHack = null;

         Uri insertedId = null;

         switch (uriMatcher.match(uri)) {

         case EVENTITEMS:

         {

             long id = db.insert(MySQLiteOpenHelper.DATABASE_EVENTS_TABLE,
                                nullColumnHack, values);
             if (id > -1) {
                // Construct and return the URI of the newly inserted row.
                insertedId = ContentUris.withAppendedId(CONTENT_URI_EVENTITEMS, id);

                // Notify any observers of the change in the data set.
                getContext().getContentResolver().notifyChange(insertedId, null);

               return insertedId;

             }
         }

         case TRIPS:
            // Log.v("Note: ", "reached here cats");
         {

             long id = db.insert(MySQLiteOpenHelper.DATABASE_TRIPS_TABLE,
                                nullColumnHack, values);
             if (id > -1) {
                // Construct and return the URI of the newly inserted row.
                insertedId = ContentUris.withAppendedId(CONTENT_URI_TRIPS, id);

                // Notify any observers of the change in the data set.
                getContext().getContentResolver().notifyChange(insertedId, null);

                return insertedId;

             }
          }
         }

        return insertedId;
       }

       @Override
       public int delete(Uri uri, String selection, String[] selectionArgs){

         // Open a read / write database to support the transaction.
         SQLiteDatabase db = myOpenHelper.getWritableDatabase();
         String DATABASE_TABLE = null;


         // If this is a row URI, limit the deletion to the specified row.
         switch (uriMatcher.match(uri)) {

         case EVENTITEMS:

         {
             String rowID = uri.getPathSegments().get(1);
               selection = KEY_ID + "=" + rowID
                   + (!TextUtils.isEmpty(selection) ?
                   " AND (" + selection + ')' : "");

             DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_EVENTS_TABLE;
         }

         case EVENTITEMS_ID:
             DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_EVENTS_TABLE;

         case TRIPS:
         {
             String rowID = uri.getPathSegments().get(1);
               selection = KEY_ID + "=" + rowID
                   + (!TextUtils.isEmpty(selection) ?
                   " AND (" + selection + ')' : "");

           DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_TRIPS_TABLE;
         }

         case TRIPS_ID:
             DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_TRIPS_TABLE;

         default: break;

         }

         // To return the number of deleted items, you must specify a where
         // clause. To delete all rows and return a value, pass in "1".
         if (selection == null)
            selection = "1";

         // Execute the deletion.
         int deleteCount = db.delete(DATABASE_TABLE, selection, selectionArgs);

         // Notify any observers of the change in the data set.
         getContext().getContentResolver().notifyChange(uri, null);

         return deleteCount;

      }

       @Override
       public int update(Uri uri, ContentValues values,
                         String selection, String[]selectionArgs){

         // Open a read / write database to support the transaction.
         SQLiteDatabase db = myOpenHelper.getWritableDatabase();
         String DATABASE_TABLE = null;


         switch (uriMatcher.match(uri)) {

         case EVENTITEMS_ID:
         {
           String rowID = uri.getPathSegments().get(0);
           selection = KEY_ID + "=" + rowID
                 + (!TextUtils.isEmpty(selection) ?
                 " AND (" + selection + ')' : "");

           DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_EVENTS_TABLE;

           break;
         }

         case EVENTITEMS:
           DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_EVENTS_TABLE;
             break;

         case TRIPS_ID:
         {
           String rowID = uri.getPathSegments().get(0);
             selection = KEY_ID + "=" + rowID
                 + (!TextUtils.isEmpty(selection) ?
                 " AND (" + selection + ')' : "");

             DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_TRIPS_TABLE;
             break;
         }


         case TRIPS:
             DATABASE_TABLE = MySQLiteOpenHelper.DATABASE_TRIPS_TABLE;
             break;

         default: break;

       }

         // Perform the update.
         int updateCount = db.update(DATABASE_TABLE,
             values, selection, selectionArgs);

           // Notify any observers of the change in the data set.
           getContext().getContentResolver().notifyChange(uri, null);

           return updateCount;
   }

      private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

           private static final String DATABASE_NAME = "mytripsDatabase.db";
           private static final int DATABASE_VERSION = 1;
           private static final String DATABASE_EVENTS_TABLE = "mytripsEventsTable";
           private static final String DATABASE_TRIPS_TABLE = "mytripsNamesTable";

           public MySQLiteOpenHelper(Context context, String name,
                                     CursorFactory factory, int version){
               super(context, name, factory, version);
           }

           //SQL statement to create a new database.
           private static final String DATABASE_CREATE_EVENTS = "create table " +
             DATABASE_EVENTS_TABLE + " (" + KEY_ID +
                   " integer primary key autoincrement, " +
                   KEY_EVENT + " TEXT, " +
                   KEY_TRIP_NAME + " TEXT, " +
                   KEY_DAY + " TEXT, " +
                   KEY_TIME_FROM + " INTEGER, " +
                   KEY_TIME_TO + " INTEGER, " +
                   KEY_CREATED + " LONG);";

           private static final String DATABASE_CREATE_TRIPS = "create table " +
                   DATABASE_TRIPS_TABLE + " (" + KEY_ID +
                   " integer primary key autoincrement, " +
                   KEY_TRIP_NAME + " TEXT, " +
                   KEY_NUM_OF_DAYS + " INTEGER);";


           //Called when no database exists on disk and the helper class needs to
             //create a new one
             @Override
             public void onCreate(SQLiteDatabase db){
                     //Create database tables
                     //db.execSQL(DATABASE_CREATE);
                     db.execSQL(DATABASE_CREATE_EVENTS);

                   db.execSQL(DATABASE_CREATE_TRIPS);
                   Log.v("here", "is reached");
             }

          //Called when there is a database version mismatch meaning that the
          //version of the databases on disk needs to be upgraded to the current
          //version
             @Override
             public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
                 //upgrade database
                 // Log the version upgrade.
                  Log.w("TaskDBAdapter", "Upgrading from version " +
                                        oldVersion + " to " +
                                       newVersion + ", which will destroy all old data");

                 //Upgrade the existing database to conform to the new version. Multiple
                 //previous versions can be handled by comparing oldVersion and newVersion
                 //values.

                 //The simplest case is to drop the old table and create a new one.
                 //db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);

                 db.execSQL("DROP TABLE IF EXISTS " + DATABASE_EVENTS_TABLE);
                    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TRIPS_TABLE);

                 //Create a new one.
                 onCreate(db);

           }

           }

   }
