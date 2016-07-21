package com.fortysevendeg.ninecardslauncher.process.social

import com.fortysevendeg.ninecardslauncher.services.plus.GooglePlusServicesException

import scalaz.Scalaz._

case class SocialProfileProcessException(
  message: String,
  cause: Option[Throwable] = None,
  recoverable: Boolean = false) extends RuntimeException(message) {

  cause foreach initCause

}

trait ImplicitsSocialProfileProcessExceptions {

  implicit def googlePlusExceptionConverter = (throwable: Throwable) => {
    throwable match {
      case gPlusException: GooglePlusServicesException =>
        SocialProfileProcessException(gPlusException.getMessage, gPlusException.some, gPlusException.recoverable)
      case _ => SocialProfileProcessException(throwable.getMessage, throwable.some)
    }
  }

}