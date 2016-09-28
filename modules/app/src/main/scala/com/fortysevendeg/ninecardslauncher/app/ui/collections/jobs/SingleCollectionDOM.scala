package com.fortysevendeg.ninecardslauncher.app.ui.collections.jobs

import android.support.v7.widget.RecyclerView.ViewHolder
import com.fortysevendeg.ninecardslauncher.app.ui.collections.CollectionAdapter
import com.fortysevendeg.ninecardslauncher.app.ui.components.dialogs.CollectionDialog
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.tweaks.PullToDownViewTweaks._
import com.fortysevendeg.ninecardslauncher.process.commons.models.{Card, Collection}
import com.fortysevendeg.ninecardslauncher.process.theme.models.NineCardsTheme
import com.fortysevendeg.ninecardslauncher2.{TR, TypedFindView}
import macroid.ContextWrapper

trait SingleCollectionDOM {

  self: TypedFindView =>

  lazy val emptyCollectionView = findView(TR.collection_detail_empty)

  lazy val emptyCollectionMessage = findView(TR.collection_empty_message)

  lazy val recyclerView = findView(TR.collection_detail_recycler)

  lazy val pullToCloseView = findView(TR.collection_detail_pull_to_close)

  def getAdapter: Option[CollectionAdapter] = recyclerView.getAdapter match {
    case a: CollectionAdapter => Some(a)
    case _ => None
  }

  def showCollectionDialog(
    moments: Seq[Collection],
    onCollection: (Int) => Any)(implicit contextWrapper: ContextWrapper, theme: NineCardsTheme): Unit = {
    new CollectionDialog(moments, onCollection, () => ()).show()
  }

  def isPulling: Boolean = (pullToCloseView ~> pdvIsPulling()).get

  def getCurrentCollection: Option[Collection] = getAdapter map (_.collection)

}

trait SingleCollectionUiListener {

  def reorderCard(collectionId: Int, cardId: Int, position: Int): Unit

  def scrollY(dy: Int): Unit

  def scrollStateChanged(idDragging: Boolean, isIdle: Boolean): Unit

  def close(): Unit

  def pullToClose(scroll: Int, scrollType: ScrollType, close: Boolean): Unit

  def reloadCards(): Unit

  def moveToCollection(toCollectionId: Int, collectionPosition: Int): Unit

  def firstItemInCollection(): Unit

  def emptyCollection(): Unit

  def forceScrollType(scrollType: ScrollType): Unit

  def openReorderMode(current: ScrollType, canScroll: Boolean): Unit

  def closeReorderMode(position: Int): Unit

  def performCard(card: Card, position: Int): Unit

  def startReorderCards(holder: ViewHolder): Unit

}