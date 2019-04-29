package org.amarcel.app.functions.hello

import java.util.function.Function

import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class HelloFunctionConfiguration {

  @Bean(Array(HelloFunctionConfiguration.FUNCTION_NAME))
  def hello(): Function[Person, WelcomeMessage] = {
    person: Person => {
      // should delegate to MailService
      // few lines of code only
      val message = String.format("Welcome %s", person.name)
      WelcomeMessage(message)
    }
  }
}

object HelloFunctionConfiguration {
  final val FUNCTION_NAME = "hello"
}
