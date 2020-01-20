package com.orez.nestedsettree.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "tree_map")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "insert_node",
                procedureName = "insert_node",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = UUID.class, name = "p_parent_uuid"),
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_description"),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, type = UUID.class, name = "uuid")
                }
        ),
        @NamedStoredProcedureQuery(
                name = "delete_node",
                procedureName = "delete_node",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = UUID.class, name = "p_node_uuid"),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "result")
                }
        ),
        @NamedStoredProcedureQuery(
                name = "move_node",
                procedureName = "move_node",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = UUID.class, name = "p_node_uuid"),
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = UUID.class, name = "p_to_parent_uuid"),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "result")
                }
        )
})
public class Node {

    @Id
    @Column(name = "node_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID nodeId;

    @Column(name = "lft", nullable = false)
    private Integer lft;

    @Column(name = "rgt", nullable = false)
    private Integer rgt;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "description")
    private String description;
}
