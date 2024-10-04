package com.basic.dto.output;


public record GenericResponse (String message, Object data, Boolean success) {

    public static final class Builder {

        String message;
        Object data;
        boolean success;


        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }


        public GenericResponse build() {
            return new GenericResponse(message, data, success);
        }
    }
}

