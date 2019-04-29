package org.amarcel.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
  * @see <a href="https://cloud.spring.io/spring-cloud-static/spring-cloud-function/2.1.0.RC1/index.html#_functional_bean_definitions">How to define beans?</a>
  */
@SpringBootApplication
@ComponentScan(lazyInit = true)
class SpringBootApp {
}

object SpringBootApp {
  def main(args: Array[String]): Unit = {
    val bootstrapClasses: Array[Class[_]] = Array(classOf[SpringBootApp])
    SpringApplication.run(bootstrapClasses, args)
  }
}
