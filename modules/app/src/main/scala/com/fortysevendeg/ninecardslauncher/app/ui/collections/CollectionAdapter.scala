package com.fortysevendeg.ninecardslauncher.app.ui.collections

import android.support.v7.widget.{CardView, RecyclerView}
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.collections.jobs.{EditingCollectionMode, GroupCollectionsJobs}
import com.fortysevendeg.ninecardslauncher.app.ui.collections.styles.CollectionAdapterStyles
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ops.TaskServiceOps._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.UiContext
import com.fortysevendeg.ninecardslauncher.app.ui.components.commons.ReorderItemTouchListener
import com.fortysevendeg.ninecardslauncher.app.ui.components.drawables.{BackgroundSelectedDrawable, IconTypes, PathMorphDrawable}
import com.fortysevendeg.ninecardslauncher.app.ui.preferences.commons.{FontSize, IconsSize, NineCardsPreferencesValue, ShowPositionInCards}
import com.fortysevendeg.ninecardslauncher.commons.ops.SeqOps._
import com.fortysevendeg.ninecardslauncher.process.commons.models.{Card, Collection}
import com.fortysevendeg.ninecardslauncher.process.commons.types._
import com.fortysevendeg.ninecardslauncher.process.intents.LauncherExecutorProcessPermissionException
import com.fortysevendeg.ninecardslauncher.process.theme.models.NineCardsTheme
import com.fortysevendeg.ninecardslauncher2.TypedResource._
import com.fortysevendeg.ninecardslauncher2.{R, TR, TypedFindView}
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui, _}

case class CollectionAdapter(var collection: Collection, heightCard: Int)
  (implicit activityContext: ActivityContextWrapper,
    uiContext: UiContext[_],
    theme: NineCardsTheme,
    collectionPresenter: CollectionPresenter,
    groupCollectionsJobs: GroupCollectionsJobs)
  extends RecyclerView.Adapter[ViewHolderCollectionAdapter]
  with ReorderItemTouchListener { self =>

  val showPositions = ShowPositionInCards.readValue(new NineCardsPreferencesValue)

  override def onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollectionAdapter = {
    val view = LayoutInflater.from(parent.getContext).inflate(TR.layout.card_item, parent, false)
    ViewHolderCollectionAdapter(
      content = view,
      heightCard = heightCard,
      showPositions = showPositions)
  }

  override def getItemCount: Int = collection.cards.size

  override def onBindViewHolder(viewHolder: ViewHolderCollectionAdapter, position: Int): Unit =
    viewHolder.bind(collection.cards(position)).run

  def addCards(cards: Seq[Card]) = {
    collection = collection.copy(cards = collection.cards ++ cards)
    val count = cards.length
    notifyItemRangeInserted(collection.cards.length - count, count)
  }

  def removeCards(cards: Seq[Card]) = {
    val cardIds = cards map (_.id)
    collection = collection.copy(cards = collection.cards.filterNot(c => cardIds.contains(c.id)))
    notifyDataSetChanged()
  }

  def updateCard(card: Card) = {
    val position = card.position
    collection = collection.copy(cards = collection.cards.updated(position, card))
    notifyItemChanged(position, card)
  }

  def updateCards(cards: Seq[Card]) = {
    collection = collection.copy(cards = cards)
    notifyItemRangeChanged(0, cards.length)
  }

  override def onItemMove(from: Int, to: Int): Unit = {
    collection = collection.copy(cards = collection.cards.reorder(from, to))
    notifyItemMoved(from, to)
  }
}

case class ViewHolderCollectionAdapter(
  content: CardView,
  heightCard: Int,
  showPositions: Boolean)
  (implicit context: ActivityContextWrapper,
    theme: NineCardsTheme,
    collectionPresenter: CollectionPresenter,
    groupCollectionsJobs: GroupCollectionsJobs)
  extends RecyclerView.ViewHolder(content)
  with CollectionAdapterStyles
  with TypedFindView {

  lazy val iconContent = findView(TR.card_icon_content)

  lazy val icon = findView(TR.card_icon)

  lazy val name = findView(TR.card_text)

  lazy val badge = findView(TR.card_badge)

  lazy val selectedIcon = findView(TR.card_selected)

  val iconSelectedDrawable = PathMorphDrawable(
    defaultIcon = IconTypes.CHECK,
    defaultStroke = resGetDimensionPixelSize(R.dimen.stroke_thin),
    padding = resGetDimensionPixelSize(R.dimen.padding_small))

  val selectedBackground = new BackgroundSelectedDrawable

  ((content <~
    rootStyle(heightCard) <~
    On.longClick {
      Ui {
        collectionPresenter.startReorderCards(this)
        true
      }
    }) ~
    (selectedIcon <~ vBackground(selectedBackground)) ~
    (iconContent <~ iconContentStyle(heightCard))).run

  def bind(card: Card)(implicit uiContext: UiContext[_]): Ui[_] = {
    val selectedViewUi = groupCollectionsJobs.statuses.collectionMode match {
      case EditingCollectionMode => selectCard(groupCollectionsJobs.statuses.positionsEditing.contains(getAdapterPosition))
      case _ => if (selectedIcon.getVisibility == View.VISIBLE) clearSelectedCard() else Ui.nop
    }
    val text = if (showPositions) s"${card.position} - ${card.term}" else card.term
    (content <~ On.click {
      Ui(groupCollectionsJobs.performCard(card, getAdapterPosition).resolveAsyncServiceOr { (e: Throwable) =>
        e match {
          case _: LauncherExecutorProcessPermissionException if card.cardType == PhoneCardType =>
            groupCollectionsJobs.requestCallPhonePermission(card.intent.extractPhone())
          case _ => groupCollectionsJobs.showGenericError()
        }
      })
    }) ~
      (icon <~ vResize(IconsSize.getIconApp) <~ iconCardTransform(card)) ~
      (name <~ tvText(text) <~ tvSizeResource(FontSize.getSizeResource) <~ nameStyle(card.cardType)) ~
      (badge <~ (getBadge(card.cardType) map {
        ivSrc(_) + vVisible
      } getOrElse vGone)) ~
      selectedViewUi
  }

  def selectCard(select: Boolean) = {
    selectedBackground.selected(select)
    selectedIcon <~ vVisible <~ (if (select) ivSrc(iconSelectedDrawable) else ivBlank)
  }

  def clearSelectedCard() = selectedIcon <~ vGone

  private[this] def getBadge(cardType: CardType): Option[Int] = cardType match {
    case PhoneCardType => Option(R.drawable.badge_phone)
    case SmsCardType => Option(R.drawable.badge_sms)
    case EmailCardType => Option(R.drawable.badge_email)
    case _ => None
  }

  override def findViewById(id: Int): View = content.findViewById(id)

}
