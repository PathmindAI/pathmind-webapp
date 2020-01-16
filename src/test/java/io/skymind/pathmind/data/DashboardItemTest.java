package io.skymind.pathmind.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DashboardItemTest {
	private static final LocalDateTime NOW = LocalDateTime.now();
	private static final LocalDateTime ONE_DAY_AGO = LocalDateTime.now().minusDays(1);
	private static final LocalDateTime ONE_MINUTE_AGO = LocalDateTime.now().minusMinutes(1);

	@Test
	@DisplayName("Should return recent date if each active date is present")
	public void allLastActivityDatesArePresent() {
		Project p = new Project();
		Model m = new Model();
		Experiment e = new Experiment();

		p.setLastActivityDate(NOW);
		m.setLastActivityDate(ONE_DAY_AGO);
		e.setLastActivityDate(ONE_MINUTE_AGO);

		DashboardItem item = new DashboardItem(p, m, e);

		assertEquals(NOW, item.getLatestUpdateTime());
	}

	@Test
	@DisplayName("Should return recent date even if some activity date are nulls")
	public void oneLastActivityDateIsNull() {
		Project p = new Project();
		Model m = new Model();
		Experiment e = new Experiment();

		p.setLastActivityDate(null);
		m.setLastActivityDate(ONE_DAY_AGO);
		e.setLastActivityDate(ONE_MINUTE_AGO);

		DashboardItem item = new DashboardItem(p, m, e);

		assertEquals(ONE_MINUTE_AGO, item.getLatestUpdateTime());
	}

	@Test
	@DisplayName("Should return current date even if all active dates are nulls")
	public void allLastActiveDatesAreNulls() {
		Project p = new Project();
		Model m = new Model();
		Experiment e = new Experiment();

		p.setLastActivityDate(null);
		m.setLastActivityDate(null);
		e.setLastActivityDate(null);

		DashboardItem item = new DashboardItem(p, m, e);

		assertNotNull(item.getLatestUpdateTime());
	}

	@Test
	@DisplayName("Should return recent date even if one element is null")
	public void oneOfElementsIsNull() {
		Project p = new Project();
		Model m = new Model();
		Experiment e = null;

		p.setLastActivityDate(NOW);
		m.setLastActivityDate(ONE_MINUTE_AGO);

		DashboardItem item = new DashboardItem(p, m, e);

		assertEquals(NOW, item.getLatestUpdateTime());
	}

}