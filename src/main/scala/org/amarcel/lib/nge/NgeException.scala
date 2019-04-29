package org.amarcel.lib.nge

import enumeratum.values.{IntEnum, IntEnumEntry}

import org.amarcel.lib.nge.NgeException.ErrorType.{InvalidData, NotFound, TechnicalError}
import org.amarcel.lib.nge.NgeException.{ErrorInfo, ErrorType}

/**
  * Design: you must use `CommonNgeExceptionFactory` to create `NgeException`
  */
abstract class NgeException(val errorType: ErrorType, val error: ErrorInfo, cause: Throwable)
  extends RuntimeException(error.message, cause) {

  def this(errorType: ErrorType, error: ErrorInfo) {
    this(errorType, error, null)
  }
}

class NgeInvalidDataException(error: ErrorInfo, cause: Throwable) extends NgeException(InvalidData, error, cause) {

  def this(error: ErrorInfo) {
    this(error, null)
  }
}

class NgeNotFoundException(error: ErrorInfo, cause: Throwable) extends NgeException(NotFound, error, cause) {

  def this(error: ErrorInfo) {
    this(error, null)
  }
}

class NgeTechnicalException(error: ErrorInfo, cause: Throwable) extends NgeException(TechnicalError, error, cause) {

  def this(error: ErrorInfo) {
    this(error, null)
  }
}

object NgeException {

  /**
    * NgeErrorType
    */
  sealed abstract class ErrorType(val value: Int, val name: String) extends IntEnumEntry

  /** ErrorType with corresponding http error code.
    * Nge stands for Network Generic Error.
    */
  object ErrorType extends IntEnum[ErrorType] {
    //noinspection TypeAnnotation
    val values = findValues

    case object InvalidData extends ErrorType(400, "InvalidData")

    case object NotFound extends ErrorType(404, "NotFound")

    case object TechnicalError extends ErrorType(500, "TechnicalError")

  }

  case class ErrorInfo private(
                                /**
                                  * For i18n. The tuple (`messageKey`, `args`) is equivalent to `message`.
                                  * If the client app does not have translation for the specified messageKey or 
                                  * does not recognize the specified messageKey, it can display the fallback `message`
                                  * instead
                                  */
                                messageKey: String,
                                args: List[Any],

                                /**
                                  * Fallback message when the messageKey is not recognized by the client app
                                  */
                                message: String,

                                /**
                                  * Useful to map form errors: one invalid field by invalid form field
                                  */
                                invalidFields: List[InvalidField],
                              )

  case class InvalidField(
                           path: List[String],
                           field: String,
                           rejectedValue: Any,
                           reason: String
                         )

}
