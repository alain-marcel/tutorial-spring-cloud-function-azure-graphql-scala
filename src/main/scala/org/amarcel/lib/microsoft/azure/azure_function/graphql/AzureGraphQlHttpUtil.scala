package org.amarcel.lib.microsoft.azure.azure_function.graphql


import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8

import com.microsoft.azure.functions.{HttpRequestMessage, HttpResponseMessage}

import org.amarcel.lib.graphql.GraphQlHttpOk
import org.amarcel.lib.graphql.NgeGraphQlHttpException.wrapToGraphQlHttpException

object AzureGraphQlHttpUtil {

  private def buildHttpResponse(status: HttpStatus, azureRequest: HttpRequestMessage[_], body: Any): HttpResponseMessage = {
    val azureHttpStatus = com.microsoft.azure.functions.HttpStatus.valueOf(status.value())
    val azureResponse: HttpResponseMessage.Builder = azureRequest.createResponseBuilder(azureHttpStatus)
    azureResponse.header(CONTENT_TYPE, APPLICATION_JSON_UTF8.toString)
    azureResponse.body(body)
    azureResponse.build()
  }

  def okHttp(azureRequest: HttpRequestMessage[_], body: GraphQlHttpOk): HttpResponseMessage = {
    buildHttpResponse(OK, azureRequest, body)
  }

  def handleException(exception: Throwable, azureRequest: HttpRequestMessage[_]): HttpResponseMessage = {
    val ex = wrapToGraphQlHttpException(exception)
    buildHttpResponse(ex.status, azureRequest, ex.toExecutionResult)
  }
}
