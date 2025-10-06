package pe.com.ladc.client;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pe.com.ladc.dto.TypeExchangeResponse;

@Path("/")
@RegisterRestClient(configKey = "client.exchangeApi")
public interface ExchangeApi {

    @GET
    @Path("/tipo-cambio/today.json")
    @Produces(MediaType.APPLICATION_JSON)
    TypeExchangeResponse getExchangeRate();
}