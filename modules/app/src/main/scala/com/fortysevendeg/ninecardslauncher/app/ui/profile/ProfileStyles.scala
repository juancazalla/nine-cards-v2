package com.fortysevendeg.ninecardslauncher.app.ui.profile

import android.content.res.ColorStateList
import android.support.v7.widget.CardView
import android.view.View
import android.widget.{ImageView, Switch, TextView}
import com.fortysevendeg.macroid.extras.CardViewTweaks._
import com.fortysevendeg.macroid.extras.DeviceVersion.Lollipop
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ops.ColorOps._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.styles.CommonStyles
import com.fortysevendeg.ninecardslauncher.app.ui.components.widgets.TintableImageView
import com.fortysevendeg.ninecardslauncher.app.ui.components.widgets.tweaks.TintableImageViewTweaks._
import com.fortysevendeg.ninecardslauncher.process.theme.models._
import macroid.{ContextWrapper, Tweak}

trait ProfileStyles {

  def menuAvatarStyle(implicit context: ContextWrapper): Tweak[ImageView] =
    Lollipop ifSupportedThen {
      vCircleOutlineProvider()
    } getOrElse Tweak.blank

}

trait AccountsAdapterStyles extends CommonStyles {

  def rootStyle()(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[View] =
    vBackgroundColor(theme.get(CardLayoutBackgroundColor))

  def iconStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TintableImageView] =
    tivDefaultColor(theme.get(DrawerIconColor))
}

trait SubscriptionsAdapterStyles extends CommonStyles {

  def switchStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[Switch] = {
    val colorStateList =
      new ColorStateList(
        Array(Array(android.R.attr.state_checked), Array()),
        Array(theme.get(PrimaryColor), theme.get(CardBackgroundColor)))
    sThumbTintList(colorStateList) +
      sTrackTintList(colorStateList)
  }

}

trait EmptyProfileAdapterStyles {

  implicit val theme: NineCardsTheme

  val textAlpha = 0.8f

  def rootStyle()(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[CardView] =
    vVisible +
      cvCardBackgroundColor(theme.get(CardBackgroundColor))

  def textStyle(implicit context: ContextWrapper): Tweak[TextView] =
    tvColor(theme.get(DrawerTextColor).alpha(textAlpha))

}