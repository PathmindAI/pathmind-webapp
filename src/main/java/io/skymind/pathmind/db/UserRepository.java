package io.skymind.pathmind.db;

import io.skymind.pathmind.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	User findUserByEmailAndPassword(String email, String password);
}
