package eu.sportsfusion.common.security.boundary.rate.handler;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import java.io.*;
import java.nio.charset.StandardCharsets;

import eu.sportsfusion.common.configuration.control.Config;
import eu.sportsfusion.common.jsonapi.JsonapiDocumentReader;
import eu.sportsfusion.common.security.boundary.rate.RateLimit;
import eu.sportsfusion.common.security.boundary.rate.RateLimitService;

public class JsonParameterLimitFilterHandler extends AbstractBaseLimitFilterHandler {

    public JsonParameterLimitFilterHandler(Config config, RateLimitService rateLimitService) {
        super(config, rateLimitService);
    }

    @Override
    public void handleRateLimit(ContainerRequestContext requestContext, String clientIpAddress, RateLimit limit) throws IOException {
        // Parse the request body into a JSON object
        // Any JsonParameter rate-limited request is required to be a valid JSON document,
        // so it is acceptable for parsing errors here to abort the request.
        String requestBody = getRequestBody(requestContext);
        JsonObject jo = new JsonapiDocumentReader(Json.createReader(new StringReader(requestBody)).readObject()).getAttributes();

        // Extract the limited JSON field from the document
        String parameterName = limit.parameter();
        String parameterValue = jo.getString(parameterName, null);

        // Apply rate limiting by parameter value
        // eg: username = jon.doe@sportsfusion.co.uk
        rateLimitService.checkExceeded(limit.name(), parameterValue, getConfig(limit.name(), config));
    }

    /**
     * Read the request body to a UTF-8 String.
     * Resets the requestContext input stream to read from this String, so it can be re-read.
     *
     * @param requestContext - the request context
     * @return requestContext body content as a String
     *
     * @throws IOException
     */
    private String getRequestBody(ContainerRequestContext requestContext) throws IOException {
        // copy input stream to byte array
        InputStream is = requestContext.getEntityStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        String requestBody = baos.toString(StandardCharsets.UTF_8.name());

        // the native input stream can only be read once,
        // so reset request input stream to read from the captured request body
        requestContext.setEntityStream(new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8)));

        return requestBody;
    }

}
