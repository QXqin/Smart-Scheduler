package com.smartscheduler.app.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FixedEventDao_Impl implements FixedEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FixedEventEntity> __insertionAdapterOfFixedEventEntity;

  private final EntityDeletionOrUpdateAdapter<FixedEventEntity> __deletionAdapterOfFixedEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBySource;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FixedEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFixedEventEntity = new EntityInsertionAdapter<FixedEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `fixed_events` (`id`,`title`,`description`,`location`,`date`,`startTime`,`endTime`,`recurrenceRule`,`sourceFile`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FixedEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getLocation());
        statement.bindString(5, entity.getDate());
        statement.bindString(6, entity.getStartTime());
        statement.bindString(7, entity.getEndTime());
        if (entity.getRecurrenceRule() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getRecurrenceRule());
        }
        statement.bindString(9, entity.getSourceFile());
      }
    };
    this.__deletionAdapterOfFixedEventEntity = new EntityDeletionOrUpdateAdapter<FixedEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `fixed_events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FixedEventEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteBySource = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM fixed_events WHERE sourceFile = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM fixed_events";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<FixedEventEntity> events,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFixedEventEntity.insert(events);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FixedEventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFixedEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBySource(final String sourceFile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBySource.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, sourceFile);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteBySource.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FixedEventEntity>> getAll() {
    final String _sql = "SELECT * FROM fixed_events ORDER BY date, startTime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fixed_events"}, new Callable<List<FixedEventEntity>>() {
      @Override
      @NonNull
      public List<FixedEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrenceRule");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final List<FixedEventEntity> _result = new ArrayList<FixedEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FixedEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final String _tmpSourceFile;
            _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            _item = new FixedEventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpRecurrenceRule,_tmpSourceFile);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FixedEventEntity>> getByDate(final String date) {
    final String _sql = "SELECT * FROM fixed_events WHERE date = ? ORDER BY startTime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fixed_events"}, new Callable<List<FixedEventEntity>>() {
      @Override
      @NonNull
      public List<FixedEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrenceRule");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final List<FixedEventEntity> _result = new ArrayList<FixedEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FixedEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final String _tmpSourceFile;
            _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            _item = new FixedEventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpRecurrenceRule,_tmpSourceFile);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FixedEventEntity>> getByDateRange(final String startDate, final String endDate) {
    final String _sql = "SELECT * FROM fixed_events WHERE date BETWEEN ? AND ? ORDER BY date, startTime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fixed_events"}, new Callable<List<FixedEventEntity>>() {
      @Override
      @NonNull
      public List<FixedEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrenceRule");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final List<FixedEventEntity> _result = new ArrayList<FixedEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FixedEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final String _tmpSourceFile;
            _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            _item = new FixedEventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpRecurrenceRule,_tmpSourceFile);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
