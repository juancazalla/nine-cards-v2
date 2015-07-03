package com.fortysevendeg.ninecardslauncher.repository.repositories

import com.fortysevendeg.ninecardslauncher.repository.Conversions.toCacheCategory
import com.fortysevendeg.ninecardslauncher.repository._
import com.fortysevendeg.ninecardslauncher.repository.commons.{CacheCategoryUri, ContentResolverWrapper}
import com.fortysevendeg.ninecardslauncher.repository.model.CacheCategory
import com.fortysevendeg.ninecardslauncher.repository.provider.CacheCategoryEntity._
import com.fortysevendeg.ninecardslauncher.repository.provider.DBUtils

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

class CacheCategoryRepository(contentResolverWrapper: ContentResolverWrapper) extends DBUtils {

  def addCacheCategory(request: AddCacheCategoryRequest)(implicit executionContext: ExecutionContext): Future[AddCacheCategoryResponse] =
    tryToFuture {
      Try {
        val values = Map[String, Any](
          packageName -> request.data.packageName,
          category -> request.data.category,
          starRating -> request.data.starRating,
          numDownloads -> request.data.numDownloads,
          ratingsCount -> request.data.ratingsCount,
          commentCount -> request.data.commentCount)

        val id = contentResolverWrapper.insert(
          nineCardsUri = CacheCategoryUri,
          values = values)

        AddCacheCategoryResponse(
          cacheCategory = CacheCategory(
            id = id,
            data = request.data))

      } recover {
        case NonFatal(e) => throw RepositoryInsertException()
      }
    }

  def deleteCacheCategory(request: DeleteCacheCategoryRequest)(implicit executionContext: ExecutionContext): Future[DeleteCacheCategoryResponse] =
    tryToFuture {
      Try {
        val deleted = contentResolverWrapper.deleteById(
          nineCardsUri = CacheCategoryUri,
          id = request.cacheCategory.id)

        DeleteCacheCategoryResponse(deleted = deleted)

      } recover {
        case NonFatal(e) => throw RepositoryDeleteException()
      }
    }

  def deleteCacheCategoryByPackage(request: DeleteCacheCategoryByPackageRequest)(implicit executionContext: ExecutionContext): Future[DeleteCacheCategoryByPackageResponse] =
    tryToFuture {
      Try {
        val deleted = contentResolverWrapper.delete(
          nineCardsUri = CacheCategoryUri,
          where = s"$packageName = ?",
          whereParams = Seq(request.packageName))

        DeleteCacheCategoryByPackageResponse(deleted = deleted)

      } recover {
        case NonFatal(e) => throw RepositoryDeleteException()
      }
    }

  def fetchCacheCategories(request: FetchCacheCategoriesRequest)(implicit executionContext: ExecutionContext): Future[FetchCacheCategoriesResponse] =
    tryToFuture {
      Try {
        val cacheCategories = contentResolverWrapper.fetchAll(
          nineCardsUri = CacheCategoryUri,
          projection = allFields)(getListFromCursor(cacheCategoryEntityFromCursor)) map toCacheCategory

        FetchCacheCategoriesResponse(cacheCategories)
      } recover {
        case e: Exception =>
          FetchCacheCategoriesResponse(cacheCategories = Seq.empty)
      }
    }

  def findCacheCategoryById(request: FindCacheCategoryByIdRequest)(implicit executionContext: ExecutionContext): Future[FindCacheCategoryByIdResponse] =
    tryToFuture {
      Try {
        val cacheCategory = contentResolverWrapper.findById(
          nineCardsUri = CacheCategoryUri,
          id = request.id,
          projection = allFields)(getEntityFromCursor(cacheCategoryEntityFromCursor)) map toCacheCategory

        FindCacheCategoryByIdResponse(cacheCategory)
      } recover {
        case e: Exception =>
          FindCacheCategoryByIdResponse(cacheCategory = None)
      }
    }

  def fetchCacheCategoryByPackage(request: FetchCacheCategoryByPackageRequest)(implicit executionContext: ExecutionContext): Future[FetchCacheCategoryByPackageResponse] =
    tryToFuture {
      Try {
        val cacheCategory = contentResolverWrapper.fetch(
          nineCardsUri = CacheCategoryUri,
          projection = allFields,
          where = s"$packageName = ?",
          whereParams = Seq(request.`package`))(getEntityFromCursor(cacheCategoryEntityFromCursor)) map toCacheCategory


        FetchCacheCategoryByPackageResponse(cacheCategory)
      } recover {
        case e: Exception =>
          FetchCacheCategoryByPackageResponse(cacheCategory = None)
      }
    }

  def updateCacheCategory(request: UpdateCacheCategoryRequest)(implicit executionContext: ExecutionContext): Future[UpdateCacheCategoryResponse] =
    tryToFuture {
      Try {
        val values = Map[String, Any](
          packageName -> request.cacheCategory.data.packageName,
          category -> request.cacheCategory.data.category,
          starRating -> request.cacheCategory.data.starRating,
          numDownloads -> request.cacheCategory.data.numDownloads,
          ratingsCount -> request.cacheCategory.data.ratingsCount,
          commentCount -> request.cacheCategory.data.commentCount)

        val updated = contentResolverWrapper.updateById(
          nineCardsUri = CacheCategoryUri,
          id = request.cacheCategory.id,
          values = values
        )

        UpdateCacheCategoryResponse(updated = updated)

      } recover {
        case NonFatal(e) => throw RepositoryUpdateException()
      }
    }
}
