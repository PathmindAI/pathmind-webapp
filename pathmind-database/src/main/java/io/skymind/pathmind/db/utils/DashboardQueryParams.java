package io.skymind.pathmind.db.utils;

 import lombok.Builder;

 @Builder
 public final class DashboardQueryParams {
 	private long userId;
 	private long experimentId;
 	private int limit;
 	private int offset;
 	private QUERY_TYPE queryType;

 	public enum QUERY_TYPE {
 		FETCH_SINGLE_BY_EXPERIMENT,
 		FETCH_MULTIPLE_BY_USER
 	}

 	public long getUserId() {
 		return userId;
 	}

 	public void setUserId(long userId) {
 		this.userId = userId;
 	}

 	public long getExperimentId() {
 		return experimentId;
 	}

 	public void setExperimentId(long experimentId) {
 		this.experimentId = experimentId;
 	}

 	public int getLimit() {
 		return limit;
 	}

 	public void setLimit(int limit) {
 		this.limit = limit;
 	}

 	public int getOffset() {
 		return offset;
 	}

 	public void setOffset(int offset) {
 		this.offset = offset;
 	}

 	public QUERY_TYPE getQueryType() {
 		return queryType;
 	}

 	public void setQueryType(QUERY_TYPE queryType) {
 		this.queryType = queryType;
 	}
 }