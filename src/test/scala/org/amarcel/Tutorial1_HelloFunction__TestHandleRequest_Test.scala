package org.amarcel

import org.amarcel.app.functions.hello.{HelloFunctionConfiguration, Person, WelcomeMessage}
import org.assertj.core.api.Assertions.assertThat
import org.junit.{Ignore, Test}
import org.junit.runner.RunWith
import org.springframework.cloud.function.context.FunctionCatalog
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import javax.inject.Inject
import reactor.core.publisher.Flux

/**
  * Test the resolution of the function by Spring Cloud Function (that is by handleRequest())
  * <a href="https://cloud.spring.io/spring-cloud-static/spring-cloud-function/2.1.0.RC1/index.html#_testing_functional_applications">FunctionCatalog</a>
  */
@RunWith(classOf[SpringRunner])
@FunctionalSpringBootTest
@Ignore
class Tutorial1_HelloFunction__TestHandleRequest_Test {

  @Inject
  var catalog: FunctionCatalog = _

  @Test
  def postHelloShouldReturnMessageStartingWithWelcome(): Unit = {
    val function: java.util.function.Function[Flux[Person], Flux[WelcomeMessage]] =
      catalog.lookup(classOf[java.util.function.Function[_, _]], HelloFunctionConfiguration.FUNCTION_NAME)

    assertThat(function.apply(Flux.just[Person](Person("Alain"))).blockFirst())
      .satisfies(x =>
        assertThat(x.welcomeMessage).startsWith("Welcome ")
      )
  }
}
