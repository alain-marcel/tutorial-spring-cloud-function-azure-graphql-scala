package org.amarcel.app.azure

import java.util.Optional
import scala.compat.java8.OptionConverters._

import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler

import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}
import com.microsoft.azure.functions.{ExecutionContext, HttpMethod, HttpRequestMessage}

import org.amarcel.app.functions.hello.{HelloFunctionConfiguration, Person, WelcomeMessage}


class HelloAzureFunction extends AzureSpringBootRequestHandler[Person, WelcomeMessage] {

  @FunctionName(HelloFunctionConfiguration.FUNCTION_NAME)
  def execute(
               @HttpTrigger(name = "req",
                 methods = Array(HttpMethod.GET, HttpMethod.POST),
                 authLevel = AuthorizationLevel.ANONYMOUS
               )
               request: HttpRequestMessage[Optional[Person]],
               context: ExecutionContext): WelcomeMessage = {

    request.getBody.asScala
      .map(person => handleRequest(person, context))
      .getOrElse(WelcomeMessage("HTTP Body missing, e.g. { \"name\": \"Alain\" }"))
  }
}
