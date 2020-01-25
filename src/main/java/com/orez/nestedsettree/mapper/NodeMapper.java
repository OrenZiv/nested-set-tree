package com.orez.nestedsettree.mapper;

import com.orez.nestedsettree.model.Node;
import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Component;

@Component
public class NodeMapper {

    public NodeDTO map(Node node) {
        return NodeDTO.builder()
                .nodeId(node.getNodeId())
                .lft(node.getLft())
                .rgt(node.getRgt())
                .parentId(node.getParentId())
                .description(node.getDescription())
                .build();
    }

    public Node map(NodeDTO nodeDTO) {
        return Node.builder()
                .nodeId(nodeDTO.getNodeId())
                .lft(nodeDTO.getLft())
                .rgt(nodeDTO.getRgt())
                .parentId(nodeDTO.getParentId())
                .description(nodeDTO.getDescription())
                .build();
    }
}
