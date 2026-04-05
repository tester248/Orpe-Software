package com.orpe.consultants.dto;

import com.fasterxml.jackson.annotation.JsonCreator;



public enum StockWiseEligibility {
 OPEN,
 CLOSED;

 @JsonCreator
 public static StockWiseEligibility fromJson(String value) {
	 if (value == null) {
		 return null;
	 }

	 String normalized = value.trim();
	 if (normalized.isEmpty()) {
		 return null;
	 }

	 String upper = normalized.toUpperCase();
	 if ("CLOSE".equals(upper)) {
		 return CLOSED;
	 }

	 return StockWiseEligibility.valueOf(upper);
 }
}
