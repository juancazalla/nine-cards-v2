package com.fortysevendeg.ninecardslauncher.process.accounts

import com.fortysevendeg.ninecardslauncher.commons.services.TaskService.NineCardException

trait UserAccountsProcessException extends NineCardException

case class UserAccountsProcessExceptionImpl(message: String, cause: Option[Throwable])
  extends RuntimeException(message)
    with UserAccountsProcessException {
  cause map initCause
}

case class UserAccountsProcessPermissionException(message: String, cause: Option[Throwable])
  extends RuntimeException(message)
    with UserAccountsProcessException {
  cause map initCause
}

case class UserAccountsProcessOperationCancelledException(message: String, cause: Option[Throwable])
  extends RuntimeException(message)
    with UserAccountsProcessException {
  cause map initCause
}

trait ImplicitsAccountsProcessExceptions {

  implicit def accountsServicesExceptionConverter =
    (t: Throwable) => UserAccountsProcessExceptionImpl(t.getMessage, Option(t))
}