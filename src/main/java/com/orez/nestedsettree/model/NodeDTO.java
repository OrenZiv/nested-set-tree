package com.orez.nestedsettree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeDTO {

    private UUID nodeId;
    private Integer lft;
    private Integer rgt;
    private UUID parentId;
    private String description;
    private List<NodeDTO> children;

    public void addChild(NodeDTO node) {
        if (children == null) {
            children = new LinkedList<>();
        }

        children.add(node);
    }
}