package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.swagger.server.api.MainApiException;

import java.util.List;
import java.util.Map;

public class WrapperApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(WrapperApiVerticle.class);

    final static String WRAPPER_CLEANUP_SERVICE_ID = "Wrapper_Cleanup";
    final static String WRAPPER_GETCAPABILITIES_SERVICE_ID = "Wrapper_GetCapabilities";
    final static String WRAPPER_LOGMESSAGE_SERVICE_ID = "Wrapper_LogMessage";
    final static String WRAPPER_SENDCOMMAND_SERVICE_ID = "Wrapper_SendCommand";
    final static String WRAPPER_SETFLAGS_SERVICE_ID = "Wrapper_SetFlags";

    final WrapperApi service;

    public WrapperApiVerticle() {
        try {
            Class serviceImplClass = getClass().getClassLoader().loadClass("io.swagger.server.api.verticle.WrapperApiImpl");
            service = (WrapperApi)serviceImplClass.newInstance();
        } catch (Exception e) {
            logUnexpectedError("WrapperApiVerticle constructor", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() throws Exception {

        //Consumer for Wrapper_Cleanup
        vertx.eventBus().<JsonObject> consumer(WRAPPER_CLEANUP_SERVICE_ID).handler(message -> {
            try {
                // Workaround for #allParams section clearing the vendorExtensions map
                String serviceId = "Wrapper_Cleanup";
                service.wrapperCleanup(result -> {
                    if (result.succeeded())
                        message.reply(null);
                    else {
                        Throwable cause = result.cause();
                        manageError(message, cause, "Wrapper_Cleanup");
                    }
                });
            } catch (Exception e) {
                logUnexpectedError("Wrapper_Cleanup", e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });

        //Consumer for Wrapper_GetCapabilities
        vertx.eventBus().<JsonObject> consumer(WRAPPER_GETCAPABILITIES_SERVICE_ID).handler(message -> {
            try {
                // Workaround for #allParams section clearing the vendorExtensions map
                String serviceId = "Wrapper_GetCapabilities";
                service.wrapperGetCapabilities(result -> {
                    if (result.succeeded())
                        message.reply(new JsonObject(Json.encode(result.result())).encodePrettily());
                    else {
                        Throwable cause = result.cause();
                        manageError(message, cause, "Wrapper_GetCapabilities");
                    }
                });
            } catch (Exception e) {
                logUnexpectedError("Wrapper_GetCapabilities", e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });

        //Consumer for Wrapper_LogMessage
        vertx.eventBus().<JsonObject> consumer(WRAPPER_LOGMESSAGE_SERVICE_ID).handler(message -> {
            try {
                // Workaround for #allParams section clearing the vendorExtensions map
                String serviceId = "Wrapper_LogMessage";
                // Changed msg handling from string->object in merge
                Object msg = message.body().getJsonObject("msg");
                if(msg == null) {
                    manageError(message, new MainApiException(400, "msg is required"), serviceId);
                    return;
                }
                service.wrapperLogMessage(msg, result -> {
                    if (result.succeeded())
                        message.reply(null);
                    else {
                        Throwable cause = result.cause();
                        manageError(message, cause, "Wrapper_LogMessage");
                    }
                });
            } catch (Exception e) {
                logUnexpectedError("Wrapper_LogMessage", e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });

        //Consumer for Wrapper_SendCommand
        vertx.eventBus().<JsonObject> consumer(WRAPPER_SENDCOMMAND_SERVICE_ID).handler(message -> {
            try {
                // Workaround for #allParams section clearing the vendorExtensions map
                String serviceId = "Wrapper_SendCommand";
                String cmdParam = message.body().getString("cmd");
                if(cmdParam == null) {
                    manageError(message, new MainApiException(400, "cmd is required"), serviceId);
                    return;
                }
                String cmd = cmdParam;
                service.wrapperSendCommand(cmd, result -> {
                    if (result.succeeded())
                        message.reply(null);
                    else {
                        Throwable cause = result.cause();
                        manageError(message, cause, "Wrapper_SendCommand");
                    }
                });
            } catch (Exception e) {
                logUnexpectedError("Wrapper_SendCommand", e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });

        //Consumer for Wrapper_SetFlags
        vertx.eventBus().<JsonObject> consumer(WRAPPER_SETFLAGS_SERVICE_ID).handler(message -> {
            try {
                // Workaround for #allParams section clearing the vendorExtensions map
                String serviceId = "Wrapper_SetFlags";
                String flagsParam = message.body().getString("flags");
                if(flagsParam == null) {
                    manageError(message, new MainApiException(400, "flags is required"), serviceId);
                    return;
                }
                Object flags = Json.mapper.readValue(flagsParam, Object.class);
                service.wrapperSetFlags(flags, result -> {
                    if (result.succeeded())
                        message.reply(null);
                    else {
                        Throwable cause = result.cause();
                        manageError(message, cause, "Wrapper_SetFlags");
                    }
                });
            } catch (Exception e) {
                logUnexpectedError("Wrapper_SetFlags", e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });

    }

    private void manageError(Message<JsonObject> message, Throwable cause, String serviceName) {
        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
        if (cause instanceof MainApiException) {
            code = ((MainApiException)cause).getStatusCode();
            statusMessage = ((MainApiException)cause).getStatusMessage();
        } else {
            logUnexpectedError(serviceName, cause);
        }

        message.fail(code, statusMessage);
    }

    private void logUnexpectedError(String serviceName, Throwable cause) {
        LOGGER.error("Unexpected error in "+ serviceName, cause);
    }
}
