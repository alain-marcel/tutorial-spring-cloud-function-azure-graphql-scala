package org.amarcel.app.functions.graphql

import java.util.function.Function

import org.springframework.context.annotation.{Bean, Configuration}

import graphql.servlet.GraphQLConfiguration

import org.amarcel.lib.graphql.processor.GraphQlProcessor
import org.amarcel.lib.graphql.{GraphQlHttpRequest, GraphQlHttpOk}

@Configuration
class GraphQlFunctionConfiguration {

  @Bean
  def graphQlProcessor(graphQLServletConfiguration: GraphQLConfiguration): GraphQlProcessor = {
    new GraphQlProcessor(graphQLServletConfiguration)
  }

  @Bean(Array(GraphQlFunctionConfiguration.FUNCTION_NAME))
  def graphQl(graphQlProcessor: GraphQlProcessor): Function[GraphQlHttpRequest, GraphQlHttpOk] = {
    graphQlRequest => graphQlProcessor.process(graphQlRequest)
  }
}

object GraphQlFunctionConfiguration {
  final val FUNCTION_NAME = "graphql"
}
