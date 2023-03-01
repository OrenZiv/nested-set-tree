package com.orez.nestedsettree.service;

import com.orez.nestedsettree.exception.NodeNotFoundException;
import com.orez.nestedsettree.exception.NodesCorruptedException;
import com.orez.nestedsettree.exception.NodesNotOrderedException;
import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class TreeService {

    private final NodeService nodeService;

    public TreeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public NodeDTO getAdjacencyListTree() {
        NodeDTO parentNode = nodeService.getRootNode()
                .orElseThrow(() -> new NodeNotFoundException("Root node not found!"));

        return rebuildAdjacencyListTreeFrom(parentNode);
    }

    public NodeDTO getAdjacencyListTreeInMem() {
        List<NodeDTO> nodes =  nodeService.findAll();
        NodeDTO root = nodes.stream()
                .filter(node -> node.getParentId() == null)
                .findFirst()
                .orElseThrow(() -> new NodeNotFoundException("Root node not found!"));

        return rebuildAdjacencyListTreeInMem(root, nodes);
    }

    public NodeDTO getNestedSetTree() {
        return rebuildNestedSetTree(nodeService.getSortedTreeNodes());
    }

    private NodeDTO rebuildAdjacencyListTreeFrom(NodeDTO parentNode) {
        nodeService.getChildrenOf(parentNode.getNodeId())
                .forEach(child -> {
                    parentNode.addChild(child);
                    rebuildAdjacencyListTreeFrom(child);
                });

        return parentNode;
    }

    private NodeDTO rebuildAdjacencyListTreeInMem(NodeDTO parentNode, List<NodeDTO> nodes) {
        nodes.stream().filter(child -> parentNode.getNodeId().equals(child.getParentId()))
                .forEach(child -> {
                    parentNode.addChild(child);
                    rebuildAdjacencyListTreeInMem(child, nodes);
                });

        return parentNode;
    }

    private NodeDTO rebuildNestedSetTree(List<NodeDTO> sortedTreeNodes) {
        validateNodeList(sortedTreeNodes);

        NodeDTO tree = sortedTreeNodes.get(0);
        LinkedList<NodeDTO> fifo = new LinkedList<>();
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

    private void validateNodeList(List<NodeDTO> nodes) {

        IntStream.range(1, nodes.size()).forEach(i -> {
            Integer currNodeLft = nodes.get(i).getLft();
            Integer prevNodeLft = nodes.get(i - 1).getLft();
            if (currNodeLft < prevNodeLft) {
                throw new NodesNotOrderedException("Nodes are not ordered by lft");
            }
            if (currNodeLft.equals(prevNodeLft)) {
                throw new NodesCorruptedException("lfts can't be equal, tree is corrupted");
            }
        });
    }
}
