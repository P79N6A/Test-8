package com.tydic.beijing.billing.rating.domain;

public class RatingException extends Exception {
	
 
		private static final long serialVersionUID = 1L;
		private int code;
		private String message;

		public int getCode() {
			return code;
		}



		public void setCode(int code) {
			this.code = code;
		}



		public String getMessage() {
			return message;
		}



		public void setMessage(String message) {
			this.message = message;
		}



		public RatingException(int code, String message) {
			this.code = code;
			this.message = message;
		}
		
		

		@Override
		public String toString() {
			return "ERR_CODE[" + code + "]:" + message;
		}
	 
	

}
