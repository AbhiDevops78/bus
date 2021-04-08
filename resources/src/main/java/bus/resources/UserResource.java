package bus.resources;

import bus.core.service.IUserService;
import bus.external.service.jsonplaceholder.IJsonPlaceholderService;
import bus.headers.pub.AddUserRequest;
import bus.headers.pub.AddUserResponse;
import bus.headers.pub.GetUserRequest;
import bus.persistence.domain.User;
import bus.resources.lock.ResourceWithLocking;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Slf4j
@Path("/api/v1/user")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final IUserService userService;
    private final IJsonPlaceholderService jsonPlaceholderService;
    private final ResourceWithLocking resourceWithLocking;

    @Inject
    public UserResource(IUserService userService, IJsonPlaceholderService jsonPlaceholderService, ResourceWithLocking resourceWithLocking) {
        this.userService = userService;
        this.jsonPlaceholderService = jsonPlaceholderService;
        this.resourceWithLocking = resourceWithLocking;
    }

    @GET
    public Response getUser(GetUserRequest request) {
        User user = userService.getUser(request.getId());
        return Response.ok(user).build();
    }

    @POST
    public Response addUser(AddUserRequest request) {
        User user = User.builder().name(request.getName()).build();
        userService.persist(user);

        // Call json-placeholder API and get the data
        // It is added here to demo how to use external services
        Map result = jsonPlaceholderService.getPost(1);

        return Response.ok(
                AddUserResponse
                        .builder()
                        .id(user.getId())
                        .someDataFromExternalHttpService(result)
                        .build()
        ).build();
    }


    @POST
    @Path("/with_lock")
    public Response methodWhichShouldBeLocked(AddUserRequest request) {
        Map<String, Object> result = resourceWithLocking.methodWhichShouldBeLocked(request.getName());
        return Response.ok(request).build();
    }
}
