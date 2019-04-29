package org.amarcel.lib.nge

import org.amarcel.lib.nge.NgeCommonErrorMessageKey.{CommonInvalidGraphQl, CommonInvalidJsonFormat, CommonNotFound, CommonUnexpectedError}
import org.amarcel.lib.nge.NgeException.ErrorInfo

/**
  * Each application should defined:
  * <ul>
  *   <li>Its own `ErrorMessageKey`,</li>
  *   <li>Its own `ErrorInfoFactory`.</li>
  * </ul>
  * And should use CommonErrorInfoFactory and CommonErrorMessageKey
  * for common errors.
  */
object NgeCommonExceptionFactory {

  def ngeException(exception: Throwable): NgeException = {
    exception match {
      case e: NgeException => e
      case e: Throwable => unexpectedError(e)
    }
  }

  def unexpectedError(e: Throwable): NgeTechnicalException = {
    new NgeTechnicalException(
      ErrorInfo(
        CommonUnexpectedError.toString,
        List(),
        s"Oops! An unexpected error occurred: $e",
        List()
      ),
      e
    )
  }

  def invalidJsonFormat(e: Throwable): NgeInvalidDataException = {
    new NgeInvalidDataException(
      ErrorInfo(
        CommonInvalidJsonFormat.toString,
        List(e.getMessage),
        s"Invalid JSON: ${e.getMessage}",
        List() // todo: extract invalid fields from the Circe exception
      ),
      e
    )
  }

  def invalidGraphQl(e: Throwable): NgeInvalidDataException = {
    new NgeInvalidDataException(
      ErrorInfo(
        CommonInvalidGraphQl.toString,
        List(e.getMessage),
        s"Invalid GraphQl: ${e.getMessage}",
        List() // todo: extract invalid fields from the Graphql exception 
      ),
      e
    )
  }

  def notFound(id: Any): NgeNotFoundException = {
    new NgeNotFoundException(
      ErrorInfo(
        CommonNotFound.toString,
        List(id),
        s"entity not found: $id",
        List()
      )
    )
  }
}
