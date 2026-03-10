package com.smartscheduler.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FixedEventDao _fixedEventDao;

  private volatile TodoDao _todoDao;

  private volatile ScheduledBlockDao _scheduledBlockDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `fixed_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `location` TEXT NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `recurrenceRule` TEXT, `sourceFile` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `todos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `deadlineDate` TEXT NOT NULL, `estimatedMinutes` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `isDaily` INTEGER NOT NULL DEFAULT 0, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scheduled_blocks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `todoId` INTEGER, `title` TEXT NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `note` TEXT NOT NULL, `generatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3b59abf952e5c218423ec494b039f82f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `fixed_events`");
        db.execSQL("DROP TABLE IF EXISTS `todos`");
        db.execSQL("DROP TABLE IF EXISTS `scheduled_blocks`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFixedEvents = new HashMap<String, TableInfo.Column>(9);
        _columnsFixedEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("recurrenceRule", new TableInfo.Column("recurrenceRule", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedEvents.put("sourceFile", new TableInfo.Column("sourceFile", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFixedEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFixedEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFixedEvents = new TableInfo("fixed_events", _columnsFixedEvents, _foreignKeysFixedEvents, _indicesFixedEvents);
        final TableInfo _existingFixedEvents = TableInfo.read(db, "fixed_events");
        if (!_infoFixedEvents.equals(_existingFixedEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "fixed_events(com.smartscheduler.app.data.local.FixedEventEntity).\n"
                  + " Expected:\n" + _infoFixedEvents + "\n"
                  + " Found:\n" + _existingFixedEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsTodos = new HashMap<String, TableInfo.Column>(8);
        _columnsTodos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("deadlineDate", new TableInfo.Column("deadlineDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("estimatedMinutes", new TableInfo.Column("estimatedMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("isDaily", new TableInfo.Column("isDaily", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTodos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTodos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTodos = new TableInfo("todos", _columnsTodos, _foreignKeysTodos, _indicesTodos);
        final TableInfo _existingTodos = TableInfo.read(db, "todos");
        if (!_infoTodos.equals(_existingTodos)) {
          return new RoomOpenHelper.ValidationResult(false, "todos(com.smartscheduler.app.data.local.TodoEntity).\n"
                  + " Expected:\n" + _infoTodos + "\n"
                  + " Found:\n" + _existingTodos);
        }
        final HashMap<String, TableInfo.Column> _columnsScheduledBlocks = new HashMap<String, TableInfo.Column>(8);
        _columnsScheduledBlocks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("todoId", new TableInfo.Column("todoId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledBlocks.put("generatedAt", new TableInfo.Column("generatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduledBlocks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScheduledBlocks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScheduledBlocks = new TableInfo("scheduled_blocks", _columnsScheduledBlocks, _foreignKeysScheduledBlocks, _indicesScheduledBlocks);
        final TableInfo _existingScheduledBlocks = TableInfo.read(db, "scheduled_blocks");
        if (!_infoScheduledBlocks.equals(_existingScheduledBlocks)) {
          return new RoomOpenHelper.ValidationResult(false, "scheduled_blocks(com.smartscheduler.app.data.local.ScheduledBlockEntity).\n"
                  + " Expected:\n" + _infoScheduledBlocks + "\n"
                  + " Found:\n" + _existingScheduledBlocks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3b59abf952e5c218423ec494b039f82f", "d6132e627bf43b13139291afe2b59c21");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "fixed_events","todos","scheduled_blocks");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `fixed_events`");
      _db.execSQL("DELETE FROM `todos`");
      _db.execSQL("DELETE FROM `scheduled_blocks`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FixedEventDao.class, FixedEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TodoDao.class, TodoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduledBlockDao.class, ScheduledBlockDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FixedEventDao fixedEventDao() {
    if (_fixedEventDao != null) {
      return _fixedEventDao;
    } else {
      synchronized(this) {
        if(_fixedEventDao == null) {
          _fixedEventDao = new FixedEventDao_Impl(this);
        }
        return _fixedEventDao;
      }
    }
  }

  @Override
  public TodoDao todoDao() {
    if (_todoDao != null) {
      return _todoDao;
    } else {
      synchronized(this) {
        if(_todoDao == null) {
          _todoDao = new TodoDao_Impl(this);
        }
        return _todoDao;
      }
    }
  }

  @Override
  public ScheduledBlockDao scheduledBlockDao() {
    if (_scheduledBlockDao != null) {
      return _scheduledBlockDao;
    } else {
      synchronized(this) {
        if(_scheduledBlockDao == null) {
          _scheduledBlockDao = new ScheduledBlockDao_Impl(this);
        }
        return _scheduledBlockDao;
      }
    }
  }
}
