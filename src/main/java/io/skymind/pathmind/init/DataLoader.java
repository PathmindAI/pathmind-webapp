package io.skymind.pathmind.init;

import io.skymind.pathmind.data.User;
import io.skymind.pathmind.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DataLoader
{
	private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	private UserRepository userRepository;

	public void initDatabase()
	{
		LOG.info("Loading data into database");
		LOG.info("Loading users");
		loadUserData();
		LOG.info("Completed loading data into database");
	}

	private void loadUserData()
	{
		userRepository.save(new User(1, "Steph", "steph@followsteph.com", "password"));
	}
}
