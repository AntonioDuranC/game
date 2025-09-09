package pe.com.ladc.exception;

import pe.com.ladc.util.ResponseModel;
import pe.com.ladc.exceptions.GameDontExistException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GameDontExistExceptionMapper implements ExceptionMapper<GameDontExistException> {

    @Override
    public Response toResponse(GameDontExistException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ResponseModel(exception.getMessage(),
                        Response.Status.NOT_FOUND.getStatusCode()))
                .build();
    }
}
