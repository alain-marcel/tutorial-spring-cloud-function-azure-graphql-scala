package org.amarcel.app.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

import org.amarcel.lib.graphql.NgeGraphQlHttpException.wrapToGraphQlHttpException
import org.amarcel.lib.graphql.{GraphQlHttpError, NgeGraphQlHttpException}

@ControllerAdvice
class ExceptionTranslator {

  @ExceptionHandler(Array(classOf[NgeGraphQlHttpException]))
  def ngeGraphQlHttpException(ex: NgeGraphQlHttpException): ResponseEntity[GraphQlHttpError] = {
    ResponseEntity.status(ex.status)
      .body(ex.toExecutionResult)
  }

  @ExceptionHandler(Array(classOf[Throwable]))
  def otherExceptions(ex: Throwable): ResponseEntity[GraphQlHttpError] = {
    ngeGraphQlHttpException(wrapToGraphQlHttpException(ex))
  }
}
