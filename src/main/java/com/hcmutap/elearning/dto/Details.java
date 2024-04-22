package com.hcmutap.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class Details {
	private int completed;
	private int failed;
	private List<DetailsInfo> details;
	public Details() {
		completed = 0;
		failed = 0;
	}
	public List<String> toList() {
		List<String> list = new ArrayList<>();
		if (details != null) {
			details.forEach(d -> {
				String error = d.username + ": " + d.errors.getFirst();
				list.add(error);
			});
		}
		return list;
	}
	public String getMessages() {
		if (details != null)
			failed = details.size();
		return "Thành công: " + completed + ", Thất bại: " + failed;
	}
	public void addCompleted() {
		completed++;
	}
	public void addFailed() {
		failed++;
	}
	public void addError(String username, String error) {
		if (details == null) {
			details = new ArrayList<>();
		}
		DetailsInfo detailsInfo = details.stream().filter(d -> d.username.equals(username)).findFirst().orElse(null);
		if (detailsInfo == null) {
			detailsInfo = new DetailsInfo(username);
			details.add(detailsInfo);
		}
		detailsInfo.addError(error);
		++failed;
	}

	public boolean hasUsername(String username) {
		return details.stream().anyMatch(d -> d.username.equals(username));
	}

	private static class DetailsInfo {
		private String username;
		private List<String> errors;

		private DetailsInfo() {
		}

		public DetailsInfo(String username) {
			this.username = username;
			errors = new ArrayList<>();
		}

		public void addError(String error) {
			errors.add(error);
		}

		public boolean hasError() {
			return !errors.isEmpty();
		}
	}
}
