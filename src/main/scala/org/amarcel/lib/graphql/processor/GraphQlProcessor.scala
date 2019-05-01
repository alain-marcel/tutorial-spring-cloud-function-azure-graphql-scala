package org.amarcel.lib.graphql.processor

import scala.collection.JavaConverters._

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import org.springframework.stereotype.Component

import com.fasterxml.jackson.databind.JsonNode
import graphql.ErrorType.{ExecutionAborted, ValidationError}
import graphql.GraphqlErrorBuilder.newError
import graphql._
import graphql.servlet._
import graphql.servlet.internal.GraphQLRequest

import org.amarcel.lib.nge.NgeCommonExceptionFactory.{invalidGraphQl, unexpectedError}
import org.amarcel.lib.nge.NgeException
import org.amarcel.lib.graphql.{GraphQlHttpOk, GraphQlHttpRequest, NgeGraphQlHttpException}

@Component
class GraphQlProcessor(configuration: GraphQLConfiguration) {

  println("====  GraphQlProcessor constructor ")

  /**
    * Copy / paste from `AbstractGraphQLHttpServlet.doRequest()`.
    */
  def process(request: GraphQlHttpRequest): GraphQlHttpOk = {
    val result =
      try {
        handleRequest(request)
      } catch {
        case e: Throwable => toFailedExecutionResult(e)
      }

    if ( result.getErrors != null && !result.getErrors.isEmpty ) {
      throw handleErrors(result.getErrors.asScala.toList)
    }

    handleData(result.getData)
  }

  /**
    * Copy / paste from `AbstractGraphQLHttpServlet.getHandler` lambda.
    */
  private def handleRequest(request: GraphQlHttpRequest): ExecutionResult = {
    val query: String = request.query

    if ( isBatchedQuery(query) ) {
      // see `AbstractGraphQLHttpServlet.queryBatched()`
      // issue to manage: each query might have specific variables
      throw new NotImplementedError("Batched GraphQL request not supported")
    }

//    val variables = request.variables.map(x => x.mapValues(_.asInstanceOf[AnyRef]).asJava).orNull
//    val graphQLRequest = new GraphQLRequest(query, variables, request.operationName.orNull)

    val graphQLRequest = new GraphQLRequest(query, null, null)
    handleSingleQuery(configuration.getInvocationInputFactory.create(graphQLRequest))
  }

  /**
    * Copy / paste from `AbstractGraphQLHttpServlet.query()`.
    */
  private def handleSingleQuery(invocationInput: GraphQLSingleInvocationInput): ExecutionResult = {
    configuration.getQueryInvoker.query(invocationInput)
  }

  private def handleData(data: Any): GraphQlHttpOk = {
    val objectMapper = configuration.getObjectMapper.getJacksonMapper

    GraphQlHttpOk(
      objectMapper.convertValue(data, classOf[JsonNode])
    )
  }

  private def handleErrors(errors: List[GraphQLError]): NgeGraphQlHttpException = {
    val status: HttpStatus =
      errors
        .map(error => toHttpStatus(error.getErrorType))
        .maxBy(x => x.value())

    new NgeGraphQlHttpException(status, errors)
  }

  private def toHttpStatus(errorType: ErrorClassification): HttpStatus = {
    if ( errorType == null || !errorType.isInstanceOf[ErrorType] ) {
      return INTERNAL_SERVER_ERROR
    }
    errorType.asInstanceOf[ErrorType] match {
      case ErrorType.InvalidSyntax => BAD_REQUEST
      case ValidationError => BAD_REQUEST
      case ErrorType.DataFetchingException => BAD_REQUEST
      case ErrorType.OperationNotSupported => INTERNAL_SERVER_ERROR
      case ErrorType.ExecutionAborted => INTERNAL_SERVER_ERROR
      case _ => INTERNAL_SERVER_ERROR
    }
  }

  private def toFailedExecutionResult(e: Throwable): ExecutionResult = {
    val errorInfo: (ErrorType, NgeException) =
      e match {
        case e: NgeException =>
          val errorType: ErrorType =
            e.errorType match {
              case NgeException.ErrorType.InvalidData => graphql.ErrorType.ValidationError
              case NgeException.ErrorType.NotFound => graphql.ErrorType.DataFetchingException
              case _ => graphql.ErrorType.ExecutionAborted
            }
          (errorType, e)

        case e: GraphQLException => (ValidationError, invalidGraphQl(e))
        case e: Throwable => (ExecutionAborted, unexpectedError(e))
      }

    new ExecutionResultImpl(
      newError()
        .errorType(errorInfo._1)
        .message(errorInfo._2.error.message)
        // todo: wrap errorInfo._2 to a case class sto have same serialization as described in NgeException_JciCodec.encode_NgeException
        .extensions(toMap(errorInfo._2))
        .build()
    )
  }


  private def toMap(obj: Any): java.util.Map[String, Object] = {
    configuration.getObjectMapper.getJacksonMapper
      .convertValue(obj, classOf[java.util.Map[String, Object]])
  }

  /**
    * @return true if the first non whitespace character is the beginning of an array
    */
  private def isBatchedQuery(query: String): Boolean = {
    for ( i <- 0 until query.length ) {
      val ch = query.charAt(i)
      if ( !Character.isWhitespace(ch) ) return ch == '['
    }
    false
  }
}
