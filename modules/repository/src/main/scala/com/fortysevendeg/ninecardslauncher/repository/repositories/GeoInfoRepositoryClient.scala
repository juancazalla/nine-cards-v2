package com.fortysevendeg.ninecardslauncher.repository.repositories

import android.database.Cursor
import com.fortysevendeg.ninecardslauncher.commons.GeoInfoUri
import com.fortysevendeg.ninecardslauncher.provider.DBUtils
import com.fortysevendeg.ninecardslauncher.provider.GeoInfoEntity._
import com.fortysevendeg.ninecardslauncher.repository.Conversions.toGeoInfo
import com.fortysevendeg.ninecardslauncher.repository._
import com.fortysevendeg.ninecardslauncher.repository.model.GeoInfo
import com.fortysevendeg.ninecardslauncher.utils._

import scala.concurrent.ExecutionContext
import scala.util.Try

trait GeoInfoRepositoryClient extends DBUtils {

  self: ContentResolverWrapperComponent =>

  implicit val executionContext: ExecutionContext

  def addGeoInfo: Service[AddGeoInfoRequest, AddGeoInfoResponse] =
    request =>
      tryToFuture {
        Try {
          val values = Map[String, Any](
            Constrain -> request.data.constrain,
            Occurrence -> request.data.occurrence,
            Wifi -> request.data.wifi,
            Latitude -> request.data.latitude,
            Longitude -> request.data.longitude,
            System -> request.data.system)

          val id = contentResolverWrapper.insert(
            nineCardsUri = GeoInfoUri,
            values = values)

          AddGeoInfoResponse(
            geoInfo = Some(GeoInfo(
              id = id,
              data = request.data)))

        } recover {
          case e: Exception =>
            AddGeoInfoResponse(geoInfo = None)
        }
      }

  def deleteGeoInfo: Service[DeleteGeoInfoRequest, DeleteGeoInfoResponse] =
    request =>
      tryToFuture {
        Try {
          contentResolverWrapper.deleteById(
            nineCardsUri = GeoInfoUri,
            id = request.geoInfo.id)

          DeleteGeoInfoResponse(success = true)

        } recover {
          case e: Exception =>
            DeleteGeoInfoResponse(success = false)
        }
      }

  def getAllGeoInfoItems: Service[GetAllGeoInfoItemsRequest, GetAllGeoInfoItemsResponse] =
    request =>
      tryToFuture {
        Try {
          val maybeCursor: Option[Cursor] = Option(contentResolverWrapper.query(
            nineCardsUri = GeoInfoUri,
            projection = AllFields))

          maybeCursor match {
            case Some(cursor) =>
              GetAllGeoInfoItemsResponse(
                geoInfoItems = getListFromCursor(cursor, geoInfoEntityFromCursor) map toGeoInfo)
            case _ => GetAllGeoInfoItemsResponse(geoInfoItems = Seq.empty)
          }

        } recover {
          case e: Exception =>
            GetAllGeoInfoItemsResponse(geoInfoItems = Seq.empty)
        }
      }

  def getGeoInfoById: Service[GetGeoInfoByIdRequest, GetGeoInfoByIdResponse] =
    request =>
      tryToFuture {
        Try {
          val maybeCursor: Option[Cursor] = Option(contentResolverWrapper.queryById(
            nineCardsUri = GeoInfoUri,
            id = request.id,
            projection = AllFields))

          maybeCursor match {
            case Some(cursor) =>
              GetGeoInfoByIdResponse(
                result = getEntityFromCursor(cursor, geoInfoEntityFromCursor) map toGeoInfo)
            case _ => GetGeoInfoByIdResponse(result = None)
          }

        } recover {
          case e: Exception =>
            GetGeoInfoByIdResponse(result = None)
        }
      }


  def getGeoInfoByConstrain: Service[GetGeoInfoByConstrainRequest, GetGeoInfoByConstrainResponse] =
    request =>
      tryToFuture {
        Try {
          val maybeCursor: Option[Cursor] = Option(contentResolverWrapper.query(
            nineCardsUri = GeoInfoUri,
            projection = AllFields,
            where = s"$Constrain = ?",
            whereParams = Array(request.constrain)))

          maybeCursor match {
            case Some(cursor) =>
              GetGeoInfoByConstrainResponse(
                result = getEntityFromCursor(cursor, geoInfoEntityFromCursor) map toGeoInfo)
            case _ => GetGeoInfoByConstrainResponse(result = None)
          }

        } recover {
          case e: Exception =>
            GetGeoInfoByConstrainResponse(result = None)
        }
      }

  def updateGeoInfo: Service[UpdateGeoInfoRequest, UpdateGeoInfoResponse] =
    request =>
      tryToFuture {
        Try {
          val values = Map[String, Any](
            Constrain -> request.geoInfo.data.constrain,
            Occurrence -> request.geoInfo.data.occurrence,
            Wifi -> request.geoInfo.data.wifi,
            Latitude -> request.geoInfo.data.latitude,
            Longitude -> request.geoInfo.data.longitude,
            System -> request.geoInfo.data.system)

          contentResolverWrapper.updateById(
            nineCardsUri = GeoInfoUri,
            id = request.geoInfo.id,
            values = values)

          UpdateGeoInfoResponse(success = true)

        } recover {
          case e: Exception =>
            UpdateGeoInfoResponse(success = false)
        }
      }
}
