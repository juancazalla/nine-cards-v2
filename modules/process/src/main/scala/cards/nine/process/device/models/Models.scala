package cards.nine.process.device.models

import cards.nine.models.Call
import cards.nine.models.types.{DockType, EmailCategory, PhoneCategory}
import cards.nine.process.commons.models.NineCardIntent
import cards.nine.process.device.types.WidgetResizeMode

case class LastCallsContact(
  hasContact: Boolean,
  number: String,
  title: String,
  photoUri: Option[String] = None,
  lookupKey: Option[String] = None,
  lastCallDate: Long,
  calls: Seq[Call])

case class ContactEmail(
  address: String,
  category: EmailCategory)

case class ContactPhone(
  number: String,
  category: PhoneCategory)

case class DockApp(
  name: String,
  dockType: DockType,
  intent: NineCardIntent,
  imagePath: String,
  position: Int)

case class AppsWithWidgets(
  packageName: String,
  name: String,
  widgets: Seq[Widget])

case class Widget(
  userHashCode: Option[Int],
  autoAdvanceViewId: Int,
  initialLayout: Int,
  minWidth: Int,
  minHeight: Int,
  minResizeWidth: Int,
  minResizeHeight: Int,
  className: String,
  packageName: String,
  resizeMode: WidgetResizeMode,
  updatePeriodMillis: Int,
  label: String,
  preview: Int)

case class TermCounter(
  term: String,
  count: Int)

