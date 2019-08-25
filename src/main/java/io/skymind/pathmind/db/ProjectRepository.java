package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>
{
}
