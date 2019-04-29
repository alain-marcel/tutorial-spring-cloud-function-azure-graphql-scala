package org.amarcel.lib.graphql

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

import graphql.ErrorType.ExecutionAborted
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder.newError

class NgeGraphQlHttpException(val status: HttpStatus, val errors: List[GraphQLError])
  extends RuntimeException(errors.headOption.map(_.getMessage).orNull) {

  def toExecutionResult: GraphQlHttpError = {
    GraphQlHttpError(errors)
  }

}

object NgeGraphQlHttpException {
  def wrapToGraphQlHttpException(exception: Throwable): NgeGraphQlHttpException = {
    exception match {
      case e: NgeGraphQlHttpException => e

      case cause: Throwable =>
        new NgeGraphQlHttpException(
          INTERNAL_SERVER_ERROR,
          List(
            newError()
              .errorType(ExecutionAborted)
              .message(cause.toString)
              .build()
          )
        )
    }
  }
}


