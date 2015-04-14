package com.fortysevendeg.ninecardslauncher.provider

import android.content.{ContentResolver, Context}
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.os.Handler
import com.fortysevendeg.ninecardslauncher.commons.ContentResolverProvider

class NineCardsSqlHelper(context: Context)
    extends SQLiteOpenHelper(context, NineCardsSqlHelper.DatabaseName, null, NineCardsSqlHelper.DatabaseVersion)
    with DBUtils
    with ContentResolverProvider {

  override implicit val contentResolver: ContentResolver = context.getContentResolver

  override def onCreate(db: SQLiteDatabase) = {

    db.execSQL("CREATE TABLE " + CacheCategoryEntity.Table +
        "(" + NineCardsSqlHelper.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CacheCategoryEntity.PackageName + " TEXT not null, " +
        CacheCategoryEntity.Category + " TEXT not null, " +
        CacheCategoryEntity.StarRating + " DOUBLE, " +
        CacheCategoryEntity.NumDownloads + " TEXT, " +
        CacheCategoryEntity.RatingsCount + " INTEGER, " +
        CacheCategoryEntity.CommentCount + " INTEGER )")

    db.execSQL("CREATE TABLE " + CollectionEntity.Table +
        "(" + NineCardsSqlHelper.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CollectionEntity.Position + " INTEGER not null, " +
        CollectionEntity.Name + " TEXT not null, " +
        CollectionEntity.Type + " TEXT not null, " +
        CollectionEntity.Icon + " TEXT not null, " +
        CollectionEntity.ThemedColorIndex + " INTEGER not null, " +
        CollectionEntity.AppsCategory + " TEXT, " +
        CollectionEntity.OriginalSharedCollectionId + " TEXT, " +
        CollectionEntity.SharedCollectionId + " TEXT, " +
        CollectionEntity.SharedCollectionSubscribed + " INTEGER, " +
        CollectionEntity.Constrains + " TEXT )")

    db.execSQL("CREATE TABLE " + CardEntity.Table +
        "(" + NineCardsSqlHelper.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CardEntity.Position + " INTEGER not null, " +
        CardEntity.CollectionId + " INTEGER not null, " +
        CardEntity.Term + " TEXT not null, " +
        CardEntity.PackageName + " TEXT, " +
        CardEntity.Type + " TEXT not null, " +
        CardEntity.Intent + " TEXT, " +
        CardEntity.ImagePath + " TEXT, " +
        CardEntity.StarRating + " DOUBLE, " +
        CardEntity.Micros + " INTEGER, " +
        CardEntity.Notification + " TEXT, " +
        CardEntity.NumDownloads + " TEXT )")

    db.execSQL("CREATE TABLE " + GeoInfoEntity.Table +
        "(" + NineCardsSqlHelper.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        GeoInfoEntity.Constrain + " TEXT not null, " +
        GeoInfoEntity.Occurrence + " TEXT not null, " +
        GeoInfoEntity.Wifi + " TEXT, " +
        GeoInfoEntity.Latitude + " DOUBLE, " +
        GeoInfoEntity.Longitude + " DOUBLE, " +
        GeoInfoEntity.System + " INTEGER )")

    new Handler().postDelayed(
      new Runnable() {
        override def run() = execAllVersionsDB()
      }, 0)
  }
  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = {

    (oldVersion + 1 to newVersion) foreach {
      case 2 =>
        db.execSQL("ALTER TABLE " + CardEntity.Table + " ADD COLUMN " + CardEntity.Notification + " TEXT")
      case 3 =>
        db.execSQL("ALTER TABLE " + CardEntity.Table + " ADD COLUMN " + CardEntity.Micros + " INTEGER")
      case 4 =>
        db.execSQL("ALTER TABLE " + CollectionEntity.Table + " ADD COLUMN " + CollectionEntity.SharedCollectionId + " TEXT")
        db.execSQL("ALTER TABLE " + CollectionEntity.Table + " ADD COLUMN " + CollectionEntity.OriginalSharedCollectionId + " TEXT")
        db.execSQL("ALTER TABLE " + CollectionEntity.Table + " ADD COLUMN " + CollectionEntity.SharedCollectionSubscribed + " INTEGER")
    }

    new Handler().post(
      new Runnable() {
        override def run() = execVersionsDB(oldVersion, newVersion)
      })
  }
}

object NineCardsSqlHelper {
  val Id = "_id"
  val DatabaseName = "nineCards"
  val DatabaseVersion = 4
}
