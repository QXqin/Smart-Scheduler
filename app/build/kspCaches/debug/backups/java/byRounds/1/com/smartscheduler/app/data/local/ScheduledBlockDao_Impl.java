package com.smartscheduler.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.lang.Long;
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
public final class ScheduledBlockDao_Impl implements ScheduledBlockDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduledBlockEntity> __insertionAdapterOfScheduledBlockEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduledBlockEntity> __deletionAdapterOfScheduledBlockEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduledBlockEntity> __updateAdapterOfScheduledBlockEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDateRange;

  public ScheduledBlockDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduledBlockEntity = new EntityInsertionAdapter<ScheduledBlockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scheduled_blocks` (`id`,`todoId`,`title`,`date`,`startTime`,`endTime`,`note`,`generatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledBlockEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTodoId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTodoId());
        }
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getStartTime());
        statement.bindString(6, entity.getEndTime());
        statement.bindString(7, entity.getNote());
        statement.bindLong(8, entity.getGeneratedAt());
      }
    };
    this.__deletionAdapterOfScheduledBlockEntity = new EntityDeletionOrUpdateAdapter<ScheduledBlockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `scheduled_blocks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledBlockEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduledBlockEntity = new EntityDeletionOrUpdateAdapter<ScheduledBlockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scheduled_blocks` SET `id` = ?,`todoId` = ?,`title` = ?,`date` = ?,`startTime` = ?,`endTime` = ?,`note` = ?,`generatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledBlockEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTodoId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTodoId());
        }
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getStartTime());
        statement.bindString(6, entity.getEndTime());
        statement.bindString(7, entity.getNote());
        statement.bindLong(8, entity.getGeneratedAt());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scheduled_blocks";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByDateRange = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scheduled_blocks WHERE date BETWEEN ? AND ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<ScheduledBlockEntity> blocks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScheduledBlockEntity.insert(blocks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ScheduledBlockEntity block,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScheduledBlockEntity.handle(block);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ScheduledBlockEntity block,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduledBlockEntity.handle(block);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Object deleteByDateRange(final String startDate, final String endDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByDateRange.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, startDate);
        _argIndex = 2;
        _stmt.bindString(_argIndex, endDate);
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
          __preparedStmtOfDeleteByDateRange.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScheduledBlockEntity>> getByDate(final String date) {
    final String _sql = "SELECT * FROM scheduled_blocks WHERE date = ? ORDER BY startTime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scheduled_blocks"}, new Callable<List<ScheduledBlockEntity>>() {
      @Override
      @NonNull
      public List<ScheduledBlockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTodoId = CursorUtil.getColumnIndexOrThrow(_cursor, "todoId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfGeneratedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedAt");
          final List<ScheduledBlockEntity> _result = new ArrayList<ScheduledBlockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduledBlockEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpTodoId;
            if (_cursor.isNull(_cursorIndexOfTodoId)) {
              _tmpTodoId = null;
            } else {
              _tmpTodoId = _cursor.getLong(_cursorIndexOfTodoId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpGeneratedAt;
            _tmpGeneratedAt = _cursor.getLong(_cursorIndexOfGeneratedAt);
            _item = new ScheduledBlockEntity(_tmpId,_tmpTodoId,_tmpTitle,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNote,_tmpGeneratedAt);
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
  public Flow<List<ScheduledBlockEntity>> getByDateRange(final String startDate,
      final String endDate) {
    final String _sql = "SELECT * FROM scheduled_blocks WHERE date BETWEEN ? AND ? ORDER BY date, startTime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scheduled_blocks"}, new Callable<List<ScheduledBlockEntity>>() {
      @Override
      @NonNull
      public List<ScheduledBlockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTodoId = CursorUtil.getColumnIndexOrThrow(_cursor, "todoId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfGeneratedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedAt");
          final List<ScheduledBlockEntity> _result = new ArrayList<ScheduledBlockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduledBlockEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpTodoId;
            if (_cursor.isNull(_cursorIndexOfTodoId)) {
              _tmpTodoId = null;
            } else {
              _tmpTodoId = _cursor.getLong(_cursorIndexOfTodoId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpGeneratedAt;
            _tmpGeneratedAt = _cursor.getLong(_cursorIndexOfGeneratedAt);
            _item = new ScheduledBlockEntity(_tmpId,_tmpTodoId,_tmpTitle,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNote,_tmpGeneratedAt);
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
  public Object getById(final long id,
      final Continuation<? super ScheduledBlockEntity> $completion) {
    final String _sql = "SELECT * FROM scheduled_blocks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduledBlockEntity>() {
      @Override
      @Nullable
      public ScheduledBlockEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTodoId = CursorUtil.getColumnIndexOrThrow(_cursor, "todoId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfGeneratedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedAt");
          final ScheduledBlockEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpTodoId;
            if (_cursor.isNull(_cursorIndexOfTodoId)) {
              _tmpTodoId = null;
            } else {
              _tmpTodoId = _cursor.getLong(_cursorIndexOfTodoId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStartTime;
            _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
            final String _tmpEndTime;
            _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpGeneratedAt;
            _tmpGeneratedAt = _cursor.getLong(_cursorIndexOfGeneratedAt);
            _result = new ScheduledBlockEntity(_tmpId,_tmpTodoId,_tmpTitle,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNote,_tmpGeneratedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
