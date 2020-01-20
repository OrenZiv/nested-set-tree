package com.orez.nestedsettree.dao;

import com.orez.nestedsettree.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
    List<Node> findAllByOrderByLft();

    @Modifying
    @Procedure(name = "insert_node")
    UUID insertNode(@Param("p_parent_uuid") UUID parentUuid, @Param("p_description") String description);

    @Modifying
    @Procedure(name = "delete_node")
    Integer deleteNode(@Param("p_node_uuid") UUID uuid);

    @Modifying
    @Procedure(name = "move_node")
    Integer moveNode(@Param("p_node_uuid") UUID uuid, @Param("p_to_parent_uuid") UUID toParentUuid);

}
