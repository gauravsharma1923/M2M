package com.digispice.m2m.exception.models;

public final class PreconditionFailedException extends RuntimeException {

	private static final long serialVersionUID = -6937526364401020479L;

		public PreconditionFailedException() {
	        super();
	    }

	    public PreconditionFailedException(final String message, final Throwable cause) {
	        super(message, cause);
	    }

	    public PreconditionFailedException(final String message) {
	        super(message);
	    }

	    public PreconditionFailedException(final Throwable cause) {
	        super(cause);
	    }

	}


