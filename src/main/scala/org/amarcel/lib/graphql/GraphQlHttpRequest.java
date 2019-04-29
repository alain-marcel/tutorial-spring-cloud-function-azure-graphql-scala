package org.amarcel.lib.graphql;

//case class GraphQlHttpRequest(query: String, operationName: Option[String], variables: Option[Map[String, _]])
public class GraphQlHttpRequest {
    public String query;

    public GraphQlHttpRequest() {}
    
    @Override
    public String toString() {
        return String.format("GraphQlHttpRequest{query='%s'}", query);
    }
}

