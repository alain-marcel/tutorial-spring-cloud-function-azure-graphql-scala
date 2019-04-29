package org.amarcel.app.pets.resolvers

import java.util
import scala.collection.JavaConverters._

import org.springframework.stereotype.Component

import com.coxautodev.graphql.tools.GraphQLQueryResolver

import org.amarcel.app.pets.entities.Pet

@Component
class Query extends GraphQLQueryResolver {

  def pets(): util.List[Pet] = {
    List(
      Pet(1l, "Billou", 9),
      Pet(2l, "Boullou", 99)
    ).asJava
  }

}