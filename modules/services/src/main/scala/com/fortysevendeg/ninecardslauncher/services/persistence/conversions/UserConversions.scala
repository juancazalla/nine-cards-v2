package com.fortysevendeg.ninecardslauncher.services.persistence.conversions

import com.fortysevendeg.ninecardslauncher.repository.model.{User => RepoUser, UserData => RepoUserData}
import com.fortysevendeg.ninecardslauncher.services.persistence._
import com.fortysevendeg.ninecardslauncher.services.persistence.models.User

trait UserConversions {

  def toUserSeq(user: Seq[RepoUser]): Seq[User] = user map toUser

  def toUser(user: RepoUser): User =
    User(
      id = user.id,
      userId = user.data.userId,
      email = user.data.email,
      sessionToken = user.data.sessionToken,
      installationId = user.data.installationId,
      deviceToken = user.data.deviceToken,
      androidToken = user.data.androidToken,
      androidPermission = user.data.androidPermission)

  def toRepositoryUser(user: User): RepoUser =
    RepoUser(
      id = user.id,
      data = RepoUserData(
        userId = user.userId,
        email = user.email,
        sessionToken = user.sessionToken,
        installationId = user.installationId,
        deviceToken = user.deviceToken,
        androidToken = user.androidToken,
        androidPermission = user.androidPermission))

  def toRepositoryUser(request: UpdateUserRequest): RepoUser =
    RepoUser(
      id = request.id,
      data = RepoUserData(
        userId = request.userId,
        email = request.email,
        sessionToken = request.sessionToken,
        installationId = request.installationId,
        deviceToken = request.deviceToken,
        androidToken = request.androidToken,
        androidPermission = request.androidPermission))

  def toRepositoryUserData(request: AddUserRequest): RepoUserData =
    RepoUserData(
      userId = request.userId,
      email = request.email,
      sessionToken = request.sessionToken,
      installationId = request.installationId,
      deviceToken = request.deviceToken,
      androidToken = request.androidToken,
      androidPermission = request.androidPermission)
}
