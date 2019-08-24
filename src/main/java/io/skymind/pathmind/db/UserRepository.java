package io.skymind.pathmind.db;

import io.skymind.pathmind.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom
{
//	User findUser(String email, String password);
}

interface UserRepositoryCustom
{
	User findUser(String email, String password);
}

class UserRepositoryImpl implements UserRepositoryCustom
{
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findUser(String email, String password)
	{
		Optional optional = userRepository.findAll().stream()
				.filter(user -> user.getEmail().equalsIgnoreCase(email))
				.filter(user -> user.getPassword().equals(password))
				.findAny();

		if(optional.isEmpty())
			return null;
		return (User)optional.get();
	}
}
