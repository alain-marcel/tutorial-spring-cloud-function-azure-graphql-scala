package org.amarcel.lib.graphql

import graphql.GraphQLError

case class GraphQlHttpError(errors: List[GraphQLError])
