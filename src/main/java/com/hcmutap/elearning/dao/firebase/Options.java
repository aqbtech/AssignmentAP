package com.hcmutap.elearning.dao.firebase;

public class Options {
	private String Comparison;
	private Options(String comparison) {
		Comparison = comparison;
	}
	public String getComparison() {
		return Comparison;
	}
	public static class OptionBuilder {
		private String Comparison;
		private OptionBuilder() {
		}
		public static OptionBuilder Builder() {
			return new OptionBuilder();
		}
		public OptionBuilder setEqual() {
			Comparison = "EQUAL";
			return this;
		}
		public Options build() {
			return new Options(Comparison);
		}
	}
}
