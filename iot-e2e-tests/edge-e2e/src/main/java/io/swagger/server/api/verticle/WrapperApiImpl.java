package io.swagger.server.api.verticle;

// Added 1 line in merge
import glue.WrapperGlue;

import io.swagger.server.api.MainApiException;

import io.swagger.server.api.model.LogMessage;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;
import java.util.Map;

// Added all Override annotations and method bodies in merge

// Changed from interface to class in merge
public class WrapperApiImpl implements WrapperApi
{
    // Added 1 line in merge
    private WrapperGlue _wrapperGlue= new WrapperGlue();

    //Wrapper_Cleanup
    @Override
    public void wrapperCleanup(Handler<AsyncResult<Void>> handler)
    {
        this._wrapperGlue.Cleanup(handler);
    }

    //Wrapper_GetCapabilities
    @Override
    public void wrapperGetCapabilities(Handler<AsyncResult<Object>> handler)
    {
        throw new java.lang.UnsupportedOperationException("Not supported yet");
    }

    //Wrapper_LogMessage
    @Override
    public void wrapperLogMessage(LogMessage logMessage, Handler<AsyncResult<Void>> handler)
    {
        this._wrapperGlue.outputMessage(logMessage, handler);
    }

    //Wrapper_SendCommand
    @Override
    public void wrapperSendCommand(String cmd, Handler<AsyncResult<Void>> handler)
    {
        throw new java.lang.UnsupportedOperationException("Not supported yet");
    }

    //Wrapper_SetFlags
    @Override
    public void wrapperSetFlags(Object flags, Handler<AsyncResult<Void>> handler)
    {
        throw new java.lang.UnsupportedOperationException("Not supported yet");
    }

}
