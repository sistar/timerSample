package api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/fake")
public interface ProcessResource {

    @GET
    @Path("/timerProcess")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> listTimerProcesses();

    @POST
    @Path("/timerProcess")
    @Consumes(MediaType.APPLICATION_JSON)
    public void receiveChecks(String msg);

}
