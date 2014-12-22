package com.codepoetics.octarine.json.serialisation;


import java.io.IOException;

public class JsonWritingException extends RuntimeException {

    public JsonWritingException(IOException cause) {
        super(cause);
    }

    public IOException getIOExceptionCause() {
        return (IOException) getCause();
    }
}