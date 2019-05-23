package org.amarcel.app.pets.resolvers

import java.util
import scala.collection.JavaConverters._

import org.amarcel.app.pets.entities.{Age, Animal, Pet}
import org.springframework.stereotype.Component

import com.coxautodev.graphql.tools.GraphQLQueryResolver

@Component
class Query extends GraphQLQueryResolver {

  def animal: Animal = {
    Animal(
      "pro1",
      "pro2", 
      "pro3", 
      "pro4",
      "pro5",
      "pro6", 
      "pro7",
      "pro8",
      "pro9",
      "pro10",
      "pro11", 
      "pro12",
      "pro13",
      "pro14", 
      "pro15",
//      "pro16",
//      "pro17",
//      "pro18",
//      "pro19",
//      "pro20",
//      "pro21",
//      "pro22",
//      "pro23",
//      "pro24",
//      "pro25",
    )
  }

  def pets(): util.List[Pet] = {
    List(
      Pet(1l, "Billou", 9),
      Pet(2l, "Boullou", 99)
    ).asJava
  }

}