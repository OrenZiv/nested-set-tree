package com.orez.nestedsettree.service;

import com.orez.nestedsettree.dao.NodeRepository;
import com.orez.nestedsettree.exception.DeleteNodeFailedException;
import com.orez.nestedsettree.exception.MoveNodeFailedException;
import com.orez.nestedsettree.exception.NodeNotFoundException;
import com.orez.nestedsettree.mapper.NodeMapper;
import com.orez.nestedsettree.model.Node;
import com.orez.nestedsettree.model.NodeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<NodeDTO> getNode(UUID uuid) {
        return nodeRepository.findById(uuid)
                .map(nodeMapper::map);
    }

    public List<NodeDTO> getSortedTreeNodes() {
        return nodeRepository.findAllByOrderByLftAsc()
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
        if (nodeRepository.deleteNode(uuid) != 1) {
            throw new DeleteNodeFailedException(String.format("Failed to delete node with uuid: %s", uuid));
        }
    }

    @Transactional
    public void moveNode(UUID uuid, UUID toParentUuid) {
        if (nodeRepository.moveNode(uuid, toParentUuid) != 1) {
            throw new MoveNodeFailedException(String.format("Failed to move node with uuid: %s to parent with uuid: %s", uuid, toParentUuid));
        }
    }

    @Transactional
    public void updateNodeDescription(UUID uuid, String description) {
        if(nodeRepository.updateNodeDescription(uuid, description) != 1) {
            throw new NodeNotFoundException(String.format("Failed to find node with uuid: %s", uuid));
        }
    }
}