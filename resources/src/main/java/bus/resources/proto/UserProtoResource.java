package bus.resources.proto;

import bus.headers.pub.proto.pojo.SampleRequest;
import bus.headers.pub.proto.pojo.SampleResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/api/v2/user")
@Consumes({"application/x-protobuf", "application/x-protobuf-text-format", "application/x-protobuf-json-format"})
@Produces({"application/x-protobuf", "application/x-protobuf-text-format", "application/x-protobuf-json-format"})
public class UserProtoResource {

    @POST
    public Response handleSampleRequest(
            @HeaderParam("Content-Type") @DefaultValue("application/x-protobuf") String contentType,
            SampleRequest request
    ) {
        SampleResponse response = SampleResponse.newBuilder()
                .setStatus("Ok")
                .setStringPassedInInput(request.getInput())
                .build();
        return Response.ok()
                .type(contentType)
                .entity(response)
                .build();
    }
}
