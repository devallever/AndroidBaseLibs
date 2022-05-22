/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.allever.android.lib.core.function.network.exception;

import androidx.annotation.Nullable;

import java.util.Objects;

import app.allever.android.lib.core.function.network.response.NetResponse;


/** Exception for an unexpected, non-2xx HTTP response. */
public class HttpException extends RuntimeException {
    private static String getMessage(NetResponse<?> response) {
        Objects.requireNonNull(response, "response == null");
        return "HTTP " + response.getCode() + " " + response.getMsg();
    }

    private final transient NetResponse<?> response;

    public HttpException(NetResponse<?> response) {
        super(getMessage(response));
        this.response = response;
    }

    /** HTTP status code. */
    public int code() {
        return response.getCode();
    }

    /** HTTP status message. */
    public String message() {
        return response.getMsg();
    }

    /** The full HTTP response. This may be null if the exception was serialized. */
    public @Nullable
    NetResponse<?> response() {
        return response;
    }
}
