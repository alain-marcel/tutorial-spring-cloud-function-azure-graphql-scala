package org.amarcel.lib.nge

import enumeratum.{Enum, EnumEntry}

sealed trait NgeCommonErrorMessageKey extends EnumEntry

object NgeCommonErrorMessageKey extends Enum[NgeCommonErrorMessageKey] {
  //noinspection TypeAnnotation
  val values = findValues

  case object CommonUnexpectedError extends NgeCommonErrorMessageKey

  case object CommonInvalidJsonFormat extends NgeCommonErrorMessageKey

  case object CommonInvalidGraphQl extends NgeCommonErrorMessageKey

  case object CommonNotFound extends NgeCommonErrorMessageKey
}

