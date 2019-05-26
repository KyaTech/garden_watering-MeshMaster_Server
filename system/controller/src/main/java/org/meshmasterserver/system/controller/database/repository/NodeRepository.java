package org.meshmasterserver.system.controller.database.repository;

import java.util.List;
import java.util.Optional;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node,Integer> {
	List<Node> findAll();
	Optional<Node> findById(int id);
	List<Node> findByMeshNodeId(int meshNodeId);
}
