package org.amarcel.app.azure

import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler

import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}
import com.microsoft.azure.functions.{ExecutionContext, HttpMethod, HttpRequestMessage, HttpResponseMessage}

import org.amarcel.app.functions.graphql.GraphQlFunctionConfiguration
import org.amarcel.lib.graphql.{GraphQlHttpOk, GraphQlHttpRequest}
import org.amarcel.lib.microsoft.azure.azure_function.graphql.AzureGraphQlHttpUtil.{handleException, okHttp}


class GraphQlAzureFunction extends AzureSpringBootRequestHandler[GraphQlHttpRequest, GraphQlHttpOk] {

  println("====  GraphQlAzureFunction constructor ")
  @FunctionName(GraphQlFunctionConfiguration.FUNCTION_NAME)
  def execute(
               @HttpTrigger(name = "graphQlRequest",
                 methods = Array(HttpMethod.POST),
                 authLevel = AuthorizationLevel.ANONYMOUS
               )
               azureRequest: HttpRequestMessage[GraphQlHttpRequest],
               context: ExecutionContext): HttpResponseMessage /* of GraphQlHttpOk */ = {

    println("====  GraphQlAzureFunction.execute ")

    try {
      okHttp(
        azureRequest,

        handleRequest(
          azureRequest.getBody,
          context
        )
      )
    } catch {
      case e: Throwable => handleException(e, azureRequest)
    }
  }
}



