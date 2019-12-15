package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;
import java.util.Map;

public interface WrapperApi  {
    //Wrapper_Cleanup
    void wrapperCleanup(Handler<AsyncResult<Void>> handler);

    //Wrapper_GetCapabilities
    void wrapperGetCapabilities(Handler<AsyncResult<Object>> handler);

    //Wrapper_LogMessage
    void wrapperLogMessage(Object msg, Handler<AsyncResult<Void>> handler);

    //Wrapper_SendCommand
    void wrapperSendCommand(String cmd, Handler<AsyncResult<Void>> handler);

    //Wrapper_SetFlags
    void wrapperSetFlags(Object flags, Handler<AsyncResult<Void>> handler);

}
