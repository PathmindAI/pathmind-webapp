package io.skymind.pathmind.ui.views.experiment.chart;

import com.vaadin.flow.component.charts.model.DataSeriesItem;

import io.skymind.pathmind.data.policy.RewardScore;

public class RewardScoreSeriesItem extends DataSeriesItem {
	
	private Integer episodeCount;
	
	public RewardScoreSeriesItem(RewardScore score) {
		setX(score.getIteration());
		setY(score.getMean());
		setEpisodeCount(score.getEpisodeCount());
	}

	public Integer getEpisodeCount() {
		return episodeCount;
	}

	public void setEpisodeCount(Integer episodeCount) {
		this.episodeCount = episodeCount;
		makeCustomized();
	}
	
}
