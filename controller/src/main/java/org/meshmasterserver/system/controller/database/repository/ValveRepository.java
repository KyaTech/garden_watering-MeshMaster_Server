package org.meshmasterserver.system.controller.database.repository;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.modell.ValveIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValveRepository extends JpaRepository<Valve, ValveIdentity> {
	List<Valve> findAll();
	List<Valve> findByIdentityNode(Node node);
}
