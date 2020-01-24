package com.orez.nestedsettree.service;

import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TreeService {

    private final NodeService nodeService;

    public TreeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public NodeDTO getTree() {
        return rebuildTree(nodeService.getSortedTreeNodes());
    }

    private NodeDTO rebuildTree(List<NodeDTO> sortedTreeNodes) {
        LinkedList<NodeDTO> fifo = new LinkedList<>();
        NodeDTO tree = sortedTreeNodes.get(0);
        fifo.add(tree);

        for (int i = 1; i < sortedTreeNodes.size(); i++) {
            NodeDTO currentNode = sortedTreeNodes.get(i);
            NodeDTO previousNode = fifo.getLast();

            if (currentNode.getRgt() < previousNode.getRgt()) {
                previousNode.addChild(currentNode);
                fifo.add(currentNode);
            } else {
                fifo.removeLast();
                i--;
            }
        }

        return tree;
    }
}
