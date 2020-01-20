package com.orez.nestedsettree.service;

import com.orez.nestedsettree.dao.NodeRepository;
import com.orez.nestedsettree.mapper.NodeMapper;
import com.orez.nestedsettree.model.Node;
import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;

    public NodeService(NodeRepository nodeRepository, NodeMapper nodeMapper) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
    }

    public NodeDTO getTree() {
        return rebuildTree(getSortedTreeNodes());
    }

    public Optional<NodeDTO> getNode(UUID uuid) {
        return nodeRepository.findById(uuid)
                .map(nodeMapper::map);
    }

    public List<NodeDTO> getSortedTreeNodes() {
        return nodeRepository.findAllByOrderByLft()
                .stream()
                .map(nodeMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public NodeDTO insertNode(UUID parentUuid, String description) {
        UUID uuid = nodeRepository.insertNode(parentUuid, description);
        return nodeMapper.map(nodeRepository.getOne(uuid));
    }

    @Transactional
    public void deleteNode(UUID uuid) {
        nodeRepository.deleteNode(uuid);
    }

    @Transactional
    public void moveNode(UUID uuid, UUID toParentUuid) {
        nodeRepository.moveNode(uuid, toParentUuid);
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