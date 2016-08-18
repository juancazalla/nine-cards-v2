package com.fortysevendeg.ninecardslauncher.app.ui.launcher.actions.editmoment

import android.widget.ArrayAdapter
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.SpinnerTweaks._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.UIActionsExtras._
import com.fortysevendeg.ninecardslauncher.app.ui.components.widgets.tweaks.TintableImageViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.tweaks.EditHourMomentLayoutTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.actions.{BaseActionFragment, Styles}
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.EditHourMomentLayout
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.tweaks.DialogToolbarTweaks._
import com.fortysevendeg.ninecardslauncher.process.commons.models.{Collection, Moment}
import com.fortysevendeg.ninecardslauncher.process.theme.models.{DrawerIconColor, DrawerTextColor}
import com.fortysevendeg.ninecardslauncher2.{R, TR, TypedFindView}
import macroid.FullDsl._
import macroid._

trait EditMomentActionsImpl
  extends EditMomentActions
  with Styles{

  self: TypedFindView with BaseActionFragment =>

  implicit val editPresenter: EditMomentPresenter

  lazy val momentCollection = findView(TR.edit_moment_collection)

  lazy val hourContent = findView(TR.edit_moment_hour_content)

  lazy val addHour = findView(TR.edit_moment_add_hour)

  lazy val wifiContent = findView(TR.edit_moment_wifi_content)

  lazy val iconLinkCollection = findView(TR.edit_moment_icon_link_collection)

  lazy val iconHour = findView(TR.edit_moment_icon_hour)

  lazy val iconWifi = findView(TR.edit_moment_icon_wifi)

  lazy val addWifi = findView(TR.edit_moment_add_wifi)

  lazy val nameWifi = findView(TR.edit_moment_name_wifi)

  lazy val nameHour = findView(TR.edit_moment_name_hour)

  lazy val nameLinkCollection = findView(TR.edit_moment_name_link_collection)

  override def initialize(moment: Moment, collections: Seq[Collection]): Ui[Any] = {
    val iconColor = theme.get(DrawerIconColor)
    val textColor = theme.get(DrawerTextColor)
    (toolbar <~
      dtbInit(colorPrimary) <~
      dtbChangeText(R.string.editMoment) <~
      dtbNavigationOnClickListener((_) => unreveal())) ~
      (iconLinkCollection <~ tivDefaultColor(iconColor)) ~
      (iconWifi <~ tivDefaultColor(iconColor)) ~
      (iconHour <~ tivDefaultColor(iconColor)) ~
      (addWifi <~ tivDefaultColor(iconColor)) ~
      (addHour <~ tivDefaultColor(iconColor)) ~
      (nameWifi <~ tvColor(textColor)) ~
      (nameHour <~ tvColor(textColor)) ~
      (nameLinkCollection <~ tvColor(textColor)) ~
      (fab <~
        fabButtonMenuStyle(colorPrimary) <~
        On.click(Ui(editPresenter.saveMoment()))) ~
      loadCategories(moment, collections) ~
      loadHours(moment)
  }

  override def momentNoFound(): Ui[Any] = unreveal()

  override def success(): Ui[Any] = unreveal()

  override def showSavingMomentErrorMessage(): Ui[Any] = uiShortToast(R.string.contactUsError)

  private[this] def loadCategories(moment: Moment, collections: Seq[Collection]): Ui[Any] = {
    val collectionIds = 0 +: (collections map (_.id))
    val collectionNames = resGetString(R.string.noLinkCollectionToMoment) +: (collections map (_.name))
    val sa = new ArrayAdapter[String](fragmentContextWrapper.getOriginal, android.R.layout.simple_spinner_dropdown_item, collectionNames.toArray)

    val spinnerPosition = moment.collectionId map collectionIds.indexOf getOrElse -1

    momentCollection <~
      sAdapter(sa) <~
      sItemSelectedListener((position) => editPresenter.setCollectionId(collectionIds.lift(position))) <~
      (if (spinnerPosition > 0) sSelection(spinnerPosition) else Tweak.blank)
  }

  private[this] def loadHours(moment: Moment): Ui[Any] = {
    val views = moment.timeslot.zipWithIndex map {
      case (slot, index) => (w[EditHourMomentLayout] <~ ehmPopulate(slot, index)).get
    }
    hourContent <~ vgAddViews(views)
  }

}
