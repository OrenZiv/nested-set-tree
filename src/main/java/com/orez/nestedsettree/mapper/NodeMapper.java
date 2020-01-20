package com.orez.nestedsettree.mapper;

import com.orez.nestedsettree.model.Node;
import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Component;

@Component
public class NodeMapper {

    public NodeDTO map (Node node) {
        return NodeDTO.builder()
                .nodeId(node.getNodeId())
                .lft(node.getLft())
                .rgt(node.getRgt())
                .parentId(node.getParentId())
                .description(node.getDescription())
                .build();
    }
}
