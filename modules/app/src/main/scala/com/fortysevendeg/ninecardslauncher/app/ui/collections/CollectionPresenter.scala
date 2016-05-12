package com.fortysevendeg.ninecardslauncher.app.ui.collections

import android.support.v7.widget.RecyclerView.ViewHolder
import com.fortysevendeg.ninecardslauncher.app.ui.commons.Presenter
import com.fortysevendeg.ninecardslauncher.app.ui.commons.TasksOps._
import com.fortysevendeg.ninecardslauncher.process.commons.models.{Card, Collection}
import macroid.{ActivityContextWrapper, Ui}
import com.fortysevendeg.ninecardslauncher.app.ui.commons.Constants._

import scalaz.concurrent.Task

case class CollectionPresenter(
  animateCards: Boolean,
  maybeCollection: Option[Collection],
  actions: CollectionUiActions)(implicit activityContextWrapper: ActivityContextWrapper)
  extends Presenter {

  def initialize(sType: ScrollType): Unit = {
    val canScroll = maybeCollection exists (_.cards.length > numSpaces)
    (actions.updateStatus(canScroll, sType) ~
      (maybeCollection map { collection =>
        actions.initialize(animateCards, collection)
      } getOrElse actions.showEmptyCollection())).run
  }

  def startReorderCards(holder: ViewHolder): Unit = if (!actions.isPulling()) actions.startReorder(holder).run

  def reorderCard(collectionId: Int, cardId: Int, position: Int): Unit = {
    Task.fork(di.collectionProcess.reorderCard(collectionId, cardId, position).run).resolveAsyncUi(
      onResult = (_) => actions.reloadCards()
    )
  }

  def moveToCard(): Unit = actions.showMessageNotImplemented().run // TODO change that

  def editCard(): Unit = actions.showMessageNotImplemented().run // TODO change that

  def addCards(cards: Seq[Card]): Unit = actions.addCards(cards).run

  def removeCard(card: Card): Unit = actions.removeCard(card).run

  def reloadCards(cards: Seq[Card]): Unit = actions.reloadCards(cards).run

  def bindAnimatedAdapter(): Unit = maybeCollection foreach { collection =>
    actions.bindAnimatedAdapter(animateCards, collection).run
  }

  def showData(): Unit = maybeCollection foreach (c => actions.showData(c.cards.isEmpty).run)

}

trait CollectionUiActions {

  def initialize(animateCards: Boolean, collection: Collection): Ui[Any]

  def updateStatus(canScroll: Boolean, sType: ScrollType): Ui[Any]

  def startReorder(holder: ViewHolder): Ui[Any]

  def reloadCards(): Ui[Any]

  def bindAnimatedAdapter(animateCards: Boolean, collection: Collection): Ui[Any]

  def showMessageNotImplemented(): Ui[Any]

  def showEmptyCollection(): Ui[Any]

  def addCards(cards: Seq[Card]): Ui[Any]

  def removeCard(card: Card): Ui[Any]

  def reloadCards(cards: Seq[Card]): Ui[Any]

  def showData(emptyCollection: Boolean): Ui[Any]

  def isPulling(): Boolean

}