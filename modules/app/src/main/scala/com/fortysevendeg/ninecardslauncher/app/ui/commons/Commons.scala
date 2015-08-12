package com.fortysevendeg.ninecardslauncher.app.ui.commons

import android.graphics.Color
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.ninecardslauncher.process.theme.models._
import com.fortysevendeg.ninecardslauncher2.R
import macroid.ContextWrapper

object Constants {

  val numSpaces = 9

  val numInLine = 3

  val minVelocity: Int = 250

  val maxRatioVelocity: Int = 3000

  val maxVelocity: Int = 700

  val spaceVelocity: Int = maxVelocity - minVelocity

}

object ImageResourceNamed {

  def iconCollectionWorkspace(category: String)(implicit context: ContextWrapper): Int =
    resGetDrawableIdentifier(s"icon_collection_$category") getOrElse R.drawable.icon_collection_default

  def iconCollectionDetail(category: String)(implicit context: ContextWrapper): Int =
    resGetDrawableIdentifier(s"icon_collection_${category}_detail") getOrElse R.drawable.icon_collection_default_detail

}

object ActivityResult {

  val wizard = 1

}

object AppUtils {
  def getUniqueId: Int = (System.currentTimeMillis & 0xfffffff).toInt

  def getDefaultTheme = NineCardsTheme(
    name = "light",
    styles = Seq(
      ThemeStyle(SearchBackgroundColor, Color.parseColor("#ffffff")),
      ThemeStyle(SearchPressedColor, Color.parseColor("#ff59afdd")),
      ThemeStyle(SearchGoogleColor, Color.parseColor("#a3a3a3")),
      ThemeStyle(SearchIconsColor, Color.parseColor("#646464")),
      ThemeStyle(AppDrawerPressedColor, Color.parseColor("#ffd5f2fa")),
      ThemeStyle(CollectionDetailBackgroundColor, Color.parseColor("#eeeeee")),
      ThemeStyle(CollectionDetailTextCardColor, Color.parseColor("#000000")),
      ThemeStyle(CollectionDetailCardBackgroundColor, Color.parseColor("#ffffff")),
      ThemeStyle(CollectionDetailCardBackgroundPressedColor, Color.parseColor("#000000")),
      ThemeStyle(CollectionDetailTextTabSelectedColor, Color.parseColor("#ffffff")),
      ThemeStyle(CollectionDetailTextTabDefaultColor, Color.parseColor("#80ffffff"))
    )
  )

  // TODO We should move this colors to theme
  def getIndexColor(index: Int): Int = index match {
    case 0 => R.color.collection_group_1
    case 1 => R.color.collection_group_2
    case 2 => R.color.collection_group_3
    case 3 => R.color.collection_group_4
    case 4 => R.color.collection_group_5
    case 5 => R.color.collection_group_6
    case 6 => R.color.collection_group_7
    case 7 => R.color.collection_group_8
    case _ => R.color.collection_group_9
  }
}

object ColorsUtils {

  def getColorDark(color: Int, ratio: Float = 0.1f) = {
    var colorHsv = Array(0f, 0f, 0f)
    Color.colorToHSV(color, colorHsv)
    colorHsv.update(2, math.max(colorHsv(2) - ratio, 0))
    Color.HSVToColor(colorHsv)
  }

  def setAlpha(color: Int, alpha: Float): Int = Color.argb((255 * alpha).toInt, Color.red(color), Color.green(color), Color.blue(color))

  def interpolateColors(fraction: Float, startValue: Int, endValue: Int): Int = {
    val startInt: Int = startValue
    val startA: Int = (startInt >> 24) & 0xff
    val startR: Int = (startInt >> 16) & 0xff
    val startG: Int = (startInt >> 8) & 0xff
    val startB: Int = startInt & 0xff
    val endInt: Int = endValue
    val endA: Int = (endInt >> 24) & 0xff
    val endR: Int = (endInt >> 16) & 0xff
    val endG: Int = (endInt >> 8) & 0xff
    val endB: Int = endInt & 0xff
    ((startA + (fraction * (endA - startA)).toInt) << 24) |
      ((startR + (fraction * (endR - startR)).toInt) << 16) |
      (startG + (fraction * (endG - startG)).toInt) << 8 |
      (startB + (fraction * (endB - startB)).toInt)
  }

}

object AnimationsUtils {

  def calculateDurationByVelocity(velocity: Float, defaultVelocity: Int): Int = {
    import Constants._
    velocity match {
      case 0 => defaultVelocity
      case _ => (spaceVelocity - ((math.min(math.abs(velocity), maxRatioVelocity) * spaceVelocity) / maxRatioVelocity) + minVelocity).toInt
    }
  }

}